package daon.analysis.ko.score;

import daon.analysis.ko.dict.config.Config.POSTag;
import daon.analysis.ko.dict.connect.ConnectMatrix;
import daon.analysis.ko.model.Keyword;
import daon.analysis.ko.model.Term;

/**
 * Created by mac on 2017. 1. 3..
 */
public class BaseScorer implements Scorer {

    private ConnectMatrix connectMatrix;

    private ScoreProperty scoreProperty;

    public BaseScorer(ConnectMatrix connectMatrix, ScoreProperty scoreProperty) {
        this.connectMatrix = connectMatrix;
        this.scoreProperty = scoreProperty;
    }
    
    @Override
    public float score(Term prev, Term cur) {
    	float score = 0;
    	
    	if(prev != null){

    		POSTag prevTag = getPosTag(prev, Direction.BACKWARD);
            float prevScore = prev.getScore(); //이전 누적 스코어 사용

    		POSTag curTag = getPosTag(cur, Direction.FORWARD);
            float curScore = getScore(cur);

            float tagScore;
            if(POSTag.un.equals(prevTag)){
                tagScore = connectMatrix.score(curTag);
            }else{
                tagScore = connectMatrix.score(prevTag, curTag);
            }
    		
            //이전 스코어 누적
            score = (prevScore + curScore) + (tagScore * scoreProperty.getConnectProb());
    	}else{

            POSTag curTag = getPosTag(cur, Direction.FORWARD);
            float curScore = getScore(cur);

            float tagScore = connectMatrix.score(curTag);

    		score = curScore + (tagScore * scoreProperty.getConnectProb());
    	}
    	
    	return score;
    }


    private float getScore(Term term){
        float score = 0;
        POSTag tag = term.getTag();

        if(POSTag.cp.equals(tag)){
            for(Keyword keyword : term.getKeyword().getSubWords()){

                score += keyword.getProb();
            }
        }else{
            score = term.getScore();
        }

        return score;
    }


    private POSTag getPosTag(Term term, Direction direction){
        POSTag tag = term.getTag();

        if(POSTag.cp.equals(tag)){
            //index 에러 나겠다..
            if(Direction.FORWARD.equals(direction)){
                tag = term.getKeyword().getSubWords().get(0).getTag();
            }else{
                tag = term.getKeyword().getSubWords().get(1).getTag();
            }
        }

        return tag;
    }


    enum Direction {
        FORWARD,
        BACKWARD
    }

}
