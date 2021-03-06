package daon.manager.web;

import daon.manager.model.data.AliasIndices;
import daon.manager.model.data.Index;
import daon.manager.service.AliasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by mac on 2017. 3. 8..
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/alias")
public class AliasController {

	@Autowired
	private AliasService aliasService;

	/**
	 * alias get
	 *
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public List<Index> get() throws Exception {

        List<Index> indices = aliasService.list();

		return indices;
	}

	/**
	 * alias add
	 *
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public boolean save(@RequestBody AliasIndices data) throws Exception {

		//validate
	    log.info("data : {}", data);

	    boolean isSuccess = aliasService.save(data);

		return isSuccess;
	}
}