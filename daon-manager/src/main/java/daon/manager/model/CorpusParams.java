package daon.manager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Created by mac on 2017. 4. 19..
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CorpusParams {

    private String id;

    private List<Integer> seq;

    private String keyword;

    private int from = 0;
    private int size = 10;

}
