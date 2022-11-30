package com.excel2dataupload.SureshExample.Service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {
    @Value("${app.upload.file:${user.home}}")
    public String EXCEL_FILE_PATH;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void dataExtract(String table) throws IOException {
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(new File(EXCEL_FILE_PATH));

        XSSFSheet sheet = workbook.getSheet("Employees");


        int rowNo =0;
        Row row = sheet.getRow(rowNo);



        List<String> columnNames  = (List<String>) row.cellIterator();
        List<String>   tableColumnNames = new ArrayList<>(columnNames.toArray().length);

        String cols = "";

        for (String columnName : columnNames) {
            if (cols == "") {
                cols = columnName + " varchar(20)";
            } else {


                cols = ", " + columnName + " varchar(20)";

            }
            tableColumnNames.add(cols);
        }


        String addQuery = "";
        for( String x : tableColumnNames)   {

            addQuery +=x;

        }
        System.out.println(addQuery);
        String createTable = "create table "+table + "(" +addQuery+")";
        jdbcTemplate.execute(createTable);

        while (rowNo < sheet.getLastRowNum()) {
            rowNo++;
            Row rows = sheet.getRow(rowNo);

            List<String> values = (List<String>) rows.cellIterator();
            String insertQuery = "insert into " + table + "values" + (values.toArray());
            jdbcTemplate.query(insertQuery, ResultSet::getObject);
        }


    }


}
