package com.lms.service.imp;

import com.lms.model.Borrow;
import com.lms.repository.BorrowRepository;
import com.lms.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelServiceImp implements ExcelService {

	private final BorrowRepository borrowRepository;

	@Override
	public byte[] generateExcelBytes(Specification<Borrow> spec) throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Sheet 1");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Row row = null;
		Cell cell = null;

		List<Borrow> listBorrow = borrowRepository.findAll(spec);

//		row = sheet.createRow(0);
//		cell = row.createCell(0);
//		cell.setCellValue("Dữ liệu");

		String[] arrHeader = {"Tên sách", "Số lượng", "Người mượn", "Ngày mượn", "Ngày trả", "Trang thái"};
		createHeader(arrHeader, sheet);

		for(int rowIndex = 0; rowIndex < listBorrow.size(); rowIndex++){
			int cellIndex = 0;
			Borrow borrow = listBorrow.get(rowIndex);
			row = sheet.createRow(rowIndex + 1);

			cell = row.createCell(cellIndex);
			cell.setCellValue(borrow.getBook().getName());

			cellIndex++;
			cell = row.createCell(cellIndex);
			cell.setCellValue(borrow.getQuantity());

			cellIndex++;
			cell = row.createCell(cellIndex);
			cell.setCellValue(borrow.getBorrower().getUsername());

			cellIndex++;
			cell = row.createCell(cellIndex);
			cell.setCellValue(sdf.format(borrow.getCreatedDate()));

			cellIndex++;
			cell = row.createCell(cellIndex);
			if(borrow.getReturnDate() != null){
				cell.setCellValue(sdf.format(borrow.getCreatedDate()));
			}

			String trangThai = "";
			if(borrow.getStatus() == 1){
				trangThai = "Đang mượn";
			} else {
				trangThai = "Đã trả";
			}
			cellIndex++;
			cell = row.createCell(cellIndex);
			cell.setCellValue(trangThai);
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		wb.write(outputStream);
		return outputStream.toByteArray();
	}

	@Override
	public byte[] generateReviewExcelBytes(Specification<Borrow> spec) throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("Sheet 1");

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Row row = null;
		Cell cell = null;

		List<Borrow> listBorrow = borrowRepository.findAll(spec);

		String[] arrHeader = {"Tên sách", "Đánh giá", "Nội dung", "Người đánh giá", "Ngày đánh giá"};
		createHeader(arrHeader, sheet);

		for(int rowIndex = 0; rowIndex < listBorrow.size(); rowIndex++){
			int cellIndex = 0;
			Borrow borrow = listBorrow.get(rowIndex);
			row = sheet.createRow(rowIndex + 1);

			cell = row.createCell(cellIndex);
			cell.setCellValue(borrow.getBook().getName());

			cellIndex++;
			cell = row.createCell(cellIndex);
			cell.setCellValue(borrow.getRating());

			cellIndex++;
			cell = row.createCell(cellIndex);
			cell.setCellValue(borrow.getComment());

			cellIndex++;
			cell = row.createCell(cellIndex);
			cell.setCellValue(borrow.getBorrower().getUsername());

			cellIndex++;
			cell = row.createCell(cellIndex);
			if(borrow.getReturnDate() != null){
				cell.setCellValue(sdf.format(borrow.getReviewedDate()));
			}
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		wb.write(outputStream);
		return outputStream.toByteArray();
	}

	private void createHeader(String[] arrHeader, Sheet sheet){
		Row row = sheet.createRow(0);
		Cell cell = null;
		for(int i = 0; i < arrHeader.length; i++){
			cell = row.createCell(i);
			cell.setCellValue(arrHeader[i]);
		}
	}
}
