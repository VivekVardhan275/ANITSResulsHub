package rocks.vivek275.anitsresultshub.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StudentPerformanceService {

    private final JdbcTemplate jdbcTemplate;

    public StudentPerformanceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void savePerformance(String batch, String branch, String semester, MultipartFile file) throws Exception {
        String tableName = "student_performance_" + batch + "_" + branch + "_" + semester;

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            // Build columns
            List<String> columns = new ArrayList<>();
            for (Cell cell : headerRow) {
                columns.add(cell.getStringCellValue().replace(" ", "_").toLowerCase());
            }

            // Create table if not exists
            StringBuilder createQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
            for (int i = 0; i < columns.size(); i++) {
                createQuery.append(columns.get(i)).append(" TEXT");
                if (i < columns.size() - 1) createQuery.append(", ");
            }
            createQuery.append(")");
            jdbcTemplate.execute(createQuery.toString());

            // Insert rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                List<String> values = new ArrayList<>();
                for (int j = 0; j < columns.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = (cell == null) ? "" : cell.toString();
                    values.add("'" + value.replace("'", "''") + "'");
                }

                String insertQuery = "INSERT INTO " + tableName +
                        " (" + String.join(",", columns) + ") VALUES (" + String.join(",", values) + ")";
                jdbcTemplate.execute(insertQuery);
            }
        }
    }

    public List<Map<String, Object>> fetchPerformance(String batch, String branch, String semester) {
        String tableName = "student_performance_" + batch + "_" + branch + "_" + semester;
        return jdbcTemplate.queryForList("SELECT * FROM " + tableName);
    }
}
