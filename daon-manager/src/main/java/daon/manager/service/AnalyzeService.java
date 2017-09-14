package daon.manager.service;

import daon.core.Daon;
import daon.core.model.EojeolInfo;
import daon.core.model.ModelInfo;
import daon.manager.model.data.AnalyzedEojeol;
import daon.manager.model.data.Term;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mac on 2017. 3. 9..
 */
@Slf4j
@Service
public class AnalyzeService {

	@Autowired
	private Daon daon;

	public List<AnalyzedEojeol> analyze(String text) throws IOException {

		if(StringUtils.isBlank(text)){
			return new ArrayList<>();
		}

		List<EojeolInfo> eojeols = daon.analyze(text);

		//결과 obj 구성..
		List<AnalyzedEojeol> results = eojeols.stream().map(e->{
			String surface = e.getSurface();

			List<Term> terms = e.getNodes().stream().map(node ->
				new Term(node.getSurface(), node.getKeywords())
			).collect(Collectors.toCollection(ArrayList::new));

			return new AnalyzedEojeol(surface, terms);
		}).collect(Collectors.toCollection(ArrayList::new));


		return results;
	}

}