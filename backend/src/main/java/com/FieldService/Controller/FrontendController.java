package com.FieldService.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

	@GetMapping(value = {
			"/{path:^(?!api|actuator).*$}",
			"/**/{path:^(?!api|actuator).*$}"
	})
	public String forwardFrontendRoutes() {
		return "forward:/index.html";
	}
}
