import com.google.gson.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DBController {
    DBModel db;

    @FXML
    TableView tableView;

    @FXML
    TextField textField;

    // Use the data to fill the table
    // Accept a JsonArray as the data source
    public void fillTable(JsonArray rs) throws SQLException{
        if (rs == null) {
            System.out.println("empty result set");
            return;
        }

        tableView.setItems(generateDataInMap(rs));
        tableView.setEditable(true);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.setColumnResizePolicy((param -> true));

        Callback<TableColumn<Map, String>, TableCell<Map, String>>
                cellFactoryForMap = new Callback<TableColumn<Map, String>,
                TableCell<Map, String>>() {
            @Override
            public TableCell call(TableColumn p) {
                return new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }
                    @Override
                    public Object fromString(String string) {
                        return string;
                    }
                });
            }
        };

        //create columns for tables
        JsonObject firstRow = rs.get(0).getAsJsonObject();
        Set<String> labels = firstRow.keySet();
        for (String label : labels) {
            TableColumn<Map, String> column = new TableColumn<>(label);
            column.setCellValueFactory(new MapValueFactory<>(label));
            tableView.getColumns().add(column);
        }
        return;
    }

    private ObservableList<Map> generateDataInMap(JsonArray rs){
        ObservableList<Map> data = FXCollections.observableArrayList();
        int rowCount = rs.size();
        for (int r = 0; r < rowCount; r++) {
            Set<Map.Entry<String, JsonElement>> row =
                    rs.get(r).getAsJsonObject().entrySet();
            Map<String, String> rowMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> cell : row) {
                String value = cell.getValue().getAsString();
                rowMap.put(cell.getKey(), value);
            }
            data.add(rowMap);
        }
        return data;
    }

    public void onClick(){
        String input = textField.getText();
        System.out.println(input);

        if(input.isEmpty()){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Your input is empty.");
            error.show();
            return;
        }
        try{
            textField.clear();
            tableView.getItems().clear();
            tableView.getColumns().clear();
            fillTable(db.processSQL(input));
        }catch(SQLException e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("Your input is not a valid SQL statement.");
            error.show();
            e.printStackTrace();
        }
    }

}
