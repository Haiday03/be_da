package com.lms.service;

import java.util.List;

import javax.validation.Valid;

import com.lms.dto.exception.NotFoundException;
import org.springframework.data.domain.Pageable;

import com.lms.dto.AuthorDto;
import com.lms.dto.AuthorOneDto;
import com.lms.dto.AuthorUpdateDto;
import com.lms.util.TPage;

public interface AuthorService {
	public AuthorDto save(AuthorDto authorDto);
	public List<AuthorDto> getAll() throws NotFoundException;
	public TPage<AuthorDto> getAllPageable(Pageable pageable) throws NotFoundException;
//	public List<AuthorDto> findAllByName(String name) throws NotFoundException ;
	public AuthorDto update(Long id, @Valid AuthorDto authorDto) throws NotFoundException;
	public AuthorOneDto getOne(Long id) throws NotFoundException;
	public Boolean delete(Long id) throws NotFoundException ;
}


package com.lms.service;

		import java.util.List;

		import javax.validation.Valid;

		import com.lms.dto.exception.NotFoundException;
		import org.springframework.data.domain.Pageable;

		import com.lms.dto.AuthorDto;
		import com.lms.dto.AuthorOneDto;
		import com.lms.dto.AuthorUpdateDto;
		import com.lms.util.TPage;

public interface AuthorService {
	public AuthorDto save(AuthorDto authorDto);
	public List<AuthorDto> getAll() throws NotFoundException;
	public TPage<AuthorDto> getAllPageable(Pageable pageable) throws NotFoundException;
	//	public List<AuthorDto> findAllByName(String name) throws NotFoundException ;
	public AuthorDto update(Long id, @Valid AuthorDto authorDto) throws NotFoundException;
	public AuthorOneDto getOne(Long id) throws NotFoundException;
	public Boolean delete(Long id) throws NotFoundException ;
}

package com.lms.service;

		import java.util.List;

		import javax.validation.Valid;

		import com.lms.dto.exception.NotFoundException;
		import org.springframework.data.domain.Pageable;

		import com.lms.dto.AuthorDto;
		import com.lms.dto.AuthorOneDto;
		import com.lms.dto.AuthorUpdateDto;
		import com.lms.util.TPage;

public interface AuthorService {
	public AuthorDto save(AuthorDto authorDto);
	public List<AuthorDto> getAll() throws NotFoundException;
	public TPage<AuthorDto> getAllPageable(Pageable pageable) throws NotFoundException;
	//	public List<AuthorDto> findAllByName(String name) throws NotFoundException ;
	public AuthorDto update(Long id, @Valid AuthorDto authorDto) throws NotFoundException;
	public AuthorOneDto getOne(Long id) throws NotFoundException;
	public Boolean delete(Long id) throws NotFoundException ;
}
