package com.lms.api;

import com.lms.dto.RegistirationRequest;
import com.lms.dto.TokenResponse;
import com.lms.security.filter.JwtAuthenticationFilter;
import com.lms.service.imp.UserServiceImp;
import com.lms.util.ApiPaths;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(ApiPaths.MainCtrl.CTRL)
@CrossOrigin
public class MainRestController {
	private final UserServiceImp userServiceImp;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;


	public MainRestController(UserServiceImp userServiceImp, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.userServiceImp = userServiceImp;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@PostMapping(value = "/sign-in")
	public ResponseEntity<TokenResponse> doLogin(HttpServletRequest request,
												 HttpServletResponse response) throws JsonProcessingException {
		Authentication auth = this.jwtAuthenticationFilter.attemptAuthentication(request, response);
		String token = this.jwtAuthenticationFilter.generateJwt(response, auth);
		return ResponseEntity.ok(new TokenResponse(auth.getName(), token));
	}


	@PostMapping(value = "/sign-up")
	public ResponseEntity<Boolean> signUp(@RequestBody RegistirationRequest registirationRequest) throws Exception {
		Boolean result = userServiceImp.register(registirationRequest);
		return ResponseEntity.ok(result);
	}
}
