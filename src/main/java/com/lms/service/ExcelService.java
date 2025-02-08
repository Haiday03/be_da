package com.lms.service;

import com.lms.model.Borrow;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;

public interface ExcelService {
	public byte[] generateExcelBytes(Specification<Borrow> spec) throws IOException;
	public byte[] generateReviewExcelBytes(Specification<Borrow> spec) throws IOException;
}
