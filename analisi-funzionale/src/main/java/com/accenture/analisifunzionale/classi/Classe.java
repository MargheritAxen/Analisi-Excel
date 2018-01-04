package com.accenture.analisifunzionale.classi;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.accenture.analisifunzionale.classi.liste.ListaAttributiClasse;

public class Classe {

	private String nome;
	private ListaAttributiClasse listaCampi = new ListaAttributiClasse();

	public Classe() {
	}

	public Classe(String nome) {
		super();
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ListaAttributiClasse getListaCampi() {
		return listaCampi;
	}

	public void setListaCampi(ListaAttributiClasse listaCampi) {
		this.listaCampi = listaCampi;
	}

	@Override
	public String toString() {
		String serialization = "{";
		serialization = serialization.concat("\"nomeClasse\" : \"").concat(this.getNome()).concat("\", \"campi\" : ")
				.concat(this.getListaCampi().toString()).concat("}");
		return serialization;
	}

	// Metodo per recuperare le classi dal foglio excel
	public static List<Classe> recuperaClassi(String leggiClassiEntity, String sheetName) throws IOException {

		try (FileInputStream inputStream = new FileInputStream(new File(leggiClassiEntity));) {
			try (Workbook workbook = new XSSFWorkbook(inputStream);) {
				Sheet entities = workbook.getSheet(sheetName);
				ArrayList<Cell> listaEntities = new ArrayList<>();

				for (int i = 1; i <= entities.getLastRowNum(); i++) {
					Row entitiesRow = entities.getRow(i);
					Cell cellEntities = entitiesRow.getCell(0);
					listaEntities.add(cellEntities);
				}

				return listaEntities.stream().filter(cell -> (cell.getCellTypeEnum() == CellType.STRING))
						.map(Cell::getStringCellValue).distinct().map(cell -> (new Classe(cell)))
						.collect(Collectors.toList());

			}
		}

	}

	// Metodo per recuperare le i campi delle classi dal foglio excel
	public static void recuperaCampiClassi(String classFieldList, String sheetName, List<Classe> classi)
			throws IOException {

		try (FileInputStream inputStream = new FileInputStream(new File(classFieldList));) {
			try (Workbook workbook = new XSSFWorkbook(inputStream);) {
				Sheet entities = workbook.getSheet(sheetName);

				for (int i = 1; i <= entities.getLastRowNum(); i++) {

					Row campiRow = entities.getRow(i);
					Cell nomeCampo = campiRow.getCell(1);
					Cell tipoCampo = campiRow.getCell(2);
					if (nomeCampo == null) {
						System.out.println("Vuoto!");
						System.out.println("Riga :" + String.valueOf(i));
						throw new RuntimeException("Vuoto!");

					}
					/*
					 * if (nomeCampo.getCellTypeEnum() == CellType.STRING) {
					 * 
					 * System.out.println("Incorrect cell type ".concat("nomeCampo"));
					 * System.out.println("Riga :" + String.valueOf(i)); throw new
					 * RuntimeException("Incorrect cell type!");
					 * 
					 * } if (tipoCampo.getCellTypeEnum() == CellType.STRING) {
					 * System.out.println("Incorrect cell type for ".concat("tipoCampo"));
					 * System.out.println("Riga :" + String.valueOf(i)); throw new
					 * RuntimeException("Incorrect cell type!");
					 * 
					 * }
					 */

					Classe classeEstratta = classi.stream().filter(classe -> {
						String nomeClasseCella = campiRow.getCell(0).getStringCellValue();
						return (classe.getNome().equals(nomeClasseCella));
					}).findFirst().orElse(null);
					assert (classeEstratta != null);
					String stringCellValueNomeCampo = nomeCampo.toString().trim();
					String stringCellValueTipoCampo = tipoCampo.toString().trim();

					classeEstratta.getListaCampi().add(new Campo(stringCellValueNomeCampo, stringCellValueTipoCampo));
				}
			}
		}
	}
}