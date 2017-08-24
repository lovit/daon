package daon.manager.model.data;

import daon.analysis.ko.model.Keyword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Term {

    private String surface;

    private Keyword[] keywords;

}