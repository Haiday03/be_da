package com.lms.service;

import com.lms.dto.PublisherDto;
import com.lms.dto.PublisherOneDto;
import com.lms.dto.PublisherUpdateDto;
import com.lms.dto.exception.NotFoundException;

import javax.validation.Valid;
import java.util.List;



public interface PublisherService {

    public PublisherDto save(PublisherDto PublisherDto);
    public List<PublisherDto> getAll() throws NotFoundException;
    public PublisherUpdateDto update(Long id, @Valid PublisherUpdateDto PublisherUpdateDto) throws NotFoundException;
    public PublisherOneDto getOne(Long id) throws NotFoundException;
    public Boolean delete(Long id) throws NotFoundException ;
}
