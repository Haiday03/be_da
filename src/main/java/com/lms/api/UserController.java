package com.lms.api;

import javax.validation.Valid;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.exception.NotFoundException;
import com.lms.model.Category;
import com.lms.model.LibraryUser;
import com.lms.service.imp.UserServiceImp;
import com.lms.util.ApiPaths;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.UserCtrl.CTRL)
@CrossOrigin
public class UserController {

	private final UserServiceImp userServiceImp;

	public UserController(UserServiceImp userServiceImp) {
		this.userServiceImp = userServiceImp;
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> getAll()
			throws NotFoundException {
		return ResponseEntity.ok(userServiceImp.getAll());
	}

	@GetMapping("/{username}")
	public ResponseEntity<UserDto> findByUserName(@PathVariable(name = "username", required = true) String username)
			throws NotFoundException {
		return ResponseEntity.ok(userServiceImp.findByUserName(username));
	}

	@GetMapping("/usersCount")
	public ResponseEntity<UserDashboard> getUsers()
	{
		return ResponseEntity.ok(userServiceImp.getUsersCount());
	}
	@PutMapping("/{username}")
	public ResponseEntity<Boolean> updateUser(@PathVariable(name = "username", required = true) String username,
			@Valid @RequestBody UserDto userDto) throws NotFoundException {
		return ResponseEntity.ok(userServiceImp.update(username, userDto));
	}

	@PatchMapping("/change-password")
	public ResponseEntity<Boolean> signUp(@RequestBody UserPasswordDto userPasswordDto) throws NotFoundException {

		Boolean result = userServiceImp.changePassword(userPasswordDto);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/getUsersCount")
	public ResponseEntity<UserDashboard> getUsersCount() {
		return ResponseEntity.ok(userServiceImp.getUsersCount());
	}

	@GetMapping("/usersMonthly")
	public ResponseEntity<List<Integer>> UsersMonthly(){
		return ResponseEntity.ok(userServiceImp.UsersMonthly());
	}

	@DeleteMapping("/{username}")
	public ResponseEntity<Boolean> deleteOne(@PathVariable(name = "username", required = true) String username)
			throws NotFoundException {
		return ResponseEntity.ok(userServiceImp.delete(username));
	}

	@GetMapping("/get/page")
	public ResponseEntity<Page<UserDto>> pageQuery(@Filter Specification<LibraryUser> spec, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<UserDto> userDtoPage = userServiceImp.findByCondition(spec, pageable);
		return ResponseEntity.ok(userDtoPage);
	}

	@DeleteMapping()
	public ResponseEntity<Boolean> deleteList(@RequestBody UserDto[] dtos)
			throws NotFoundException {
		return ResponseEntity.ok(userServiceImp.deleteList(dtos));
	}

}
