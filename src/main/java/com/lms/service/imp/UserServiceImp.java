package com.lms.service.imp;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.llq.springfilter.boot.Filter;
import com.lms.dto.*;
import com.lms.dto.exception.LogicalException;
import com.lms.dto.exception.NotFoundException;
import com.lms.dto.exception.UserNotFoundException;
import com.lms.model.Borrow;
import com.lms.model.Category;
import com.lms.model.LibraryUser;
import com.lms.model.Publisher;
import com.lms.model.enumerations.Role;
import com.lms.repository.BookRepository;
import com.lms.repository.BorrowRepository;
import com.lms.repository.UserRepository;
import com.lms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;
	private final BorrowRepository borrowRepository;
	private final PasswordEncoder bCryptPasswordEncoder;


	public List<UserDto> getAll() throws NotFoundException {
		List<LibraryUser> users = userRepository.findAll();
		if (users.size() < 1) {
			throw new NotFoundException("Project don't already exist");
		}
		UserDto[] userdto = modelMapper.map(users, UserDto[].class);
		return Arrays.asList(userdto);
	}

	@Transactional
	public Boolean register(RegistirationRequest registirationRequest) throws Exception {
		if (userRepository.findByUsername(registirationRequest.getUsername()).isPresent()) {
			throw new Exception("Tên đăng nhập: " + registirationRequest.getUsername() + " đã tồn tại");
		}
		LibraryUser user = new LibraryUser();
		user.setRealPassword(registirationRequest.getPassword());
		registirationRequest.setPassword(bCryptPasswordEncoder.encode(registirationRequest.getPassword()));
		user.setUserId(UUID.randomUUID().toString());
		user.setUsername(registirationRequest.getUsername());
		user.setPassword(registirationRequest.getPassword());
		user.setRealPassword(registirationRequest.getConfirmpassword());
		user.setDateCreated(registirationRequest.getDateCreated());
		user.setRole(Role.ROLE_USER);
		user.setRoleCode("USER");
		userRepository.save(user);
		return true;

	}

	public UserDto findByUserName(String username) throws NotFoundException {
		try {
			LibraryUser user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException(username));
			UserDto userDto = modelMapper.map(user, UserDto.class);
			return userDto;
		} catch (Exception e) {
			throw new NotFoundException("Tên đăng nhập: " + username + " không tồn tại");
		}
	}
	public LibraryUser findByUserId(String userId) throws NotFoundException {
		try {
			LibraryUser user = userRepository.findByUserId(userId).orElseThrow(()->new UserNotFoundException(userId));
			//UserDto userDto = modelMapper.map(user, UserDto.class);
			return user;
		} catch (Exception e) {
			throw new NotFoundException("LibraryUser doesn't exist with this name called : " + userId);
		}
	}


	public Boolean update(String username, @Valid UserDto userDto) throws NotFoundException {

		LibraryUser libraryUser = userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException(username));

		LibraryUser user = modelMapper.map(userDto, LibraryUser.class);
		user.setId(libraryUser.getId());
		user.setRealPassword(libraryUser.getRealPassword());
		user.setPassword(libraryUser.getPassword());
		user.setUserId(libraryUser.getUserId());
		user.setRole(libraryUser.getRole());
		user.setDateCreated(libraryUser.getDateCreated());
		userRepository.save(user);
		return true;
	}

	public Boolean changePassword(UserPasswordDto userPasswordDto) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(!userPasswordDto.getUsername().equals(auth.getName())) {
			return false;
		}
		
		LibraryUser libraryUser = userRepository.findByUsername(userPasswordDto.getUsername()).orElseThrow(()->new UserNotFoundException(userPasswordDto.getUsername()));
		if (libraryUser == null) {
			throw new NotFoundException("LibraryUser dosen't exist with this name called : " + userPasswordDto.getUsername());
		}
		System.out.println(libraryUser.getPassword());
		boolean control=bCryptPasswordEncoder.matches( userPasswordDto.getPassword(),libraryUser.getPassword());
		if (!control) {
			throw new NotFoundException("Your current password is incorrect.");
		}
		libraryUser.setRealPassword(userPasswordDto.getNewpassword());
		libraryUser.setPassword(bCryptPasswordEncoder.encode(userPasswordDto.getNewpassword()));
		userRepository.save(libraryUser);
		return true;

	}
	
	
	
	//IF USER IS STUDENT

	public UserDto save(@Valid UserDto UserDto) throws Exception {
		List<LibraryUser> list = userRepository.findByEmail(UserDto.getEmail().trim());

		if(list.size()>0){
			throw new Exception("LibraryUser email already exist : " + UserDto.getEmail());
		}
		LibraryUser LibraryUser = modelMapper.map(UserDto, LibraryUser.class);
		userRepository.save(LibraryUser);
		UserDto.setId(LibraryUser.getId());
		return UserDto;
	}

	public UserDto findById(Long id) throws NotFoundException {
		Optional<LibraryUser> UserOpt = userRepository.findById(id);
		if (!UserOpt.isPresent()) {
			throw new NotFoundException("LibraryUser dosen't exist");
		}
		return modelMapper.map(UserOpt.get(), UserDto.class);
	}

	public UserDto update(Long id, @Valid UserDto UserDto) throws Exception {
		Optional<LibraryUser> UserOpt = userRepository.findById(id);
		if (!UserOpt.isPresent()) {
			throw new NotFoundException("LibraryUser dosen't exist");
		}
		LibraryUser LibraryUser = modelMapper.map(UserDto, LibraryUser.class);
		LibraryUser.setId(id);
		userRepository.save(LibraryUser);
		UserDto.setId(LibraryUser.getId());
		return UserDto;
	}

	@Override
	public Boolean delete(String username) throws NotFoundException {
		Optional<LibraryUser> user = userRepository.findByUsername(username);
		if (!user.isPresent())
			throw new NotFoundException("Không tìm thấy bản ghi với username: " + username);
		List<Borrow> borrows = borrowRepository.findByCreatedUser(user.get().getId());
		if(borrows.size() > 0){
			throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
		}
		userRepository.deleteById(user.get().getId());
		return true;
	}

	public Boolean deleteList(UserDto[] dtos) throws NotFoundException {
		LibraryUser[] entities = modelMapper.map(dtos, LibraryUser[].class);
		for(UserDto userDto:dtos){
			List<Borrow> borrows = borrowRepository.findByCreatedUser(userDto.getId());
			if(borrows.size() > 0){
				throw new LogicalException("Bản ghi đang có ràng buộc về dữ liệu!");
			}
		}
		userRepository.deleteAll(Arrays.asList(entities));
		return true;
	}

	public UserDashboard getUsersCount()
	{
			UserDashboard userDashboard=new UserDashboard();

			userDashboard.setUsersCount(userRepository.findAll().size());

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7);

			userDashboard.setPercentageUsersCount(userRepository.findAll().stream().filter(f->f.getDateCreated().before(cal.getTime())).collect(Collectors.toList()).size());


		return userDashboard;
	}
	public List<Integer> UsersMonthly()
	{
		List<Integer> month= new ArrayList<>();
		int i=0;
		while(i<=6)
		{
			int current=i;
			month.add(userRepository.findAll().stream()
					.filter(u->u.getDateCreated().getMonth() ==  current)
					.collect(Collectors.toList()).size());
			i++;
		}
		return month;
	}

	public Page<UserDto> findByCondition(@Filter Specification<LibraryUser> spec, Pageable pageable) {
		Page<LibraryUser> entityPage = userRepository.findAll(spec, pageable);
		List<LibraryUser> entities = entityPage.getContent();
		return new PageImpl<>(modelMapper.map(entities, new TypeToken<List<UserDto>>() {}.getType()), pageable, entityPage.getTotalElements());
	}
}
