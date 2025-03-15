package com.lms.service;

import java.util.List;

import javax.validation.Valid;

import com.lms.dto.UserDashboard;
import com.lms.dto.UserDto;
import com.lms.dto.exception.NotFoundException;


public interface UserService {

	public UserDto save(@Valid UserDto UserDto) throws Exception;
	public List<UserDto> getAll() throws NotFoundException;
	public UserDto update(Long id, @Valid UserDto UserDto) throws Exception;
	public Boolean delete(String username) throws NotFoundException;
	public UserDashboard getUsersCount()  throws NotFoundException;
	public List<Integer> UsersMonthly()  throws NotFoundException;



}

package com.lms.service;

		import java.util.List;

		import javax.validation.Valid;

		import com.lms.dto.UserDashboard;
		import com.lms.dto.UserDto;
		import com.lms.dto.exception.NotFoundException;


public interface UserService {

	public UserDto save(@Valid UserDto UserDto) throws Exception;
	public List<UserDto> getAll() throws NotFoundException;
	public UserDto update(Long id, @Valid UserDto UserDto) throws Exception;
	public Boolean delete(String username) throws NotFoundException;
	public UserDashboard getUsersCount()  throws NotFoundException;
	public List<Integer> UsersMonthly()  throws NotFoundException;



}


package com.lms.service;

		import java.util.List;

		import javax.validation.Valid;

		import com.lms.dto.UserDashboard;
		import com.lms.dto.UserDto;
		import com.lms.dto.exception.NotFoundException;


public interface UserService {

	public UserDto save(@Valid UserDto UserDto) throws Exception;
	public List<UserDto> getAll() throws NotFoundException;
	public UserDto update(Long id, @Valid UserDto UserDto) throws Exception;
	public Boolean delete(String username) throws NotFoundException;
	public UserDashboard getUsersCount()  throws NotFoundException;
	public List<Integer> UsersMonthly()  throws NotFoundException;



}
