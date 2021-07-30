module AgendaPro {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.media;
	requires javafx.swing;
	requires javafx.web;
	
	opens application to javafx.graphics, javafx.fxml;
}
