package org.personal.batchexample.batch.batch;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.personal.batchexample.batch.entity.BeforeEntity;
import org.springframework.batch.item.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelRowWriter implements ItemStreamWriter<BeforeEntity> {

    private final String filePath;
    private Workbook workbook;
    private Sheet sheet;
    private int currentRowNumber;

    public ExcelRowWriter(String filePath) {
        this.filePath = filePath;
        currentRowNumber =0;
    }

    @Override
    public void write(Chunk<? extends BeforeEntity> chunk) throws Exception {

        for(BeforeEntity entity: chunk){
            Row row = sheet.createRow(currentRowNumber++);
            row.createCell(0).setCellValue(entity.getUsername());;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Sheet1");
    }



    @Override
    public void close() throws ItemStreamException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                workbook.close();
            }catch (IOException e){
                throw new ItemStreamException(e);
            }
        }
    }
}
