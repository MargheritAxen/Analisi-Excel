package com.accenture.analisifunzionale.classi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

	private List<Mapping> mappingEspliciti = new ArrayList<>();
	private List<Mapping> mappingImpliciti = new ArrayList<>();
	private List<Mapping> mappingMancantiFrom = new ArrayList<>();
	private List<Mapping> mappingMancantiTo = new ArrayList<>();

	private String nomeMapper;
	private String fileMapper;
	private String classeFrom;
	private String classeTo;
	private boolean hasByDefault;
	private boolean hasCustom;

	public Mapper() {
	}

	public Mapper(String nomeMapper) {
		this.nomeMapper = nomeMapper;
	}

	public Mapper(List<Mapping> mappingEspliciti, List<Mapping> mappingImpliciti, List<Mapping> mappingMancantiFrom,
			List<Mapping> mappingMancantiTo, String nomeMapper, String fileMapper, String classeFrom, String classeTo,
			boolean hasByDefault, boolean hasCustom) {
		super();
		this.mappingEspliciti = mappingEspliciti;
		this.mappingImpliciti = mappingImpliciti;
		this.mappingMancantiFrom = mappingMancantiFrom;
		this.mappingMancantiTo = mappingMancantiTo;
		this.nomeMapper = nomeMapper;
		this.fileMapper = fileMapper;
		this.classeFrom = classeFrom;
		this.classeTo = classeTo;
		this.hasByDefault = hasByDefault;
		this.hasCustom = hasCustom;
	}

	public Mapper(String fileMapper, String nomeMapper) {
		this.nomeMapper = nomeMapper;
		this.fileMapper = fileMapper;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mapper [mappingEspliciti = ");
		builder.append(mappingEspliciti);
		builder.append(", mappingImpliciti = ");
		builder.append(mappingImpliciti);
		builder.append(", mappingMancantiFrom = ");
		builder.append(mappingMancantiFrom);
		builder.append(", mappingMancantiTo = ");
		builder.append(mappingMancantiTo);
		builder.append(", nomeMapper = ");
		builder.append(nomeMapper);
		builder.append(", fileMapper = ");
		builder.append(fileMapper);
		builder.append(", classeFrom = ");
		builder.append(classeFrom);
		builder.append(", classeTo = ");
		builder.append(classeTo);
		builder.append(", hasByDefault = ");
		builder.append(hasByDefault);
		builder.append(", hasCustom = ");
		builder.append(hasCustom);
		builder.append("]");
		return builder.toString();
	}

	// Recupero il nome del file dal file excel -> RISULTATO
	public static List<Mapper> recuperaNomeFile(String leggiMapper, String sheetName) throws IOException {

		try (FileInputStream inputStream = new FileInputStream(new File(leggiMapper));) {
			try (Workbook workbook = new XSSFWorkbook(inputStream);) {
				Sheet mappers = workbook.getSheet(sheetName);
				ArrayList<Mapper> listaMappers = new ArrayList<>();

				for (int i = 1; i <= mappers.getLastRowNum(); i++) {
					Row mapperRows = mappers.getRow(i);
					Cell cellFileName = mapperRows.getCell(0);
					Cell cellClassName = mapperRows.getCell(1);
					listaMappers.add(new Mapper(cellFileName.toString(), cellClassName.toString()));
				}

				return listaMappers;
			}
		}
	}

	// Recupero classi Mapper dal file excel -> RISULTATO
	public static void popolaMapper(Sheet mappers1, Mapper mapper, List<Classe> models, List<Classe> entities)
			throws IOException {

		for (int i = 1; i <= mappers1.getLastRowNum(); i++) {

			Row fileRow = mappers1.getRow(i);
			Cell nomeFile = fileRow.getCell(0);
			Cell nomeClasse = fileRow.getCell(1);
			Cell classeModello = fileRow.getCell(2);
			Cell classeEntity = fileRow.getCell(3);
			Cell tipoMapping = fileRow.getCell(4); // tipo mapping nella tabella excel
			Cell nomeCampoModel = fileRow.getCell(5);
			Cell nomeCampoEntity = fileRow.getCell(6);
			if (nomeFile == null || nomeClasse == null || classeModello == null || classeEntity == null
					|| tipoMapping == null) {
				System.out.println("Vuoto!");
				System.out.println("Riga :" + String.valueOf(i));
				throw new RuntimeException("Vuoto!");
			}

			Mapper mapperAstratto = mapper;
			assert (mapperAstratto != null);
			String stringCellValueClasseModello = classeModello.toString().trim();
			String stringCellValueClasseEntity = classeEntity.toString().trim();
			String stringCellValueTipoMapping = tipoMapping.toString().trim();

			Classe model = models.stream().filter(modello -> (modello.getNome().equals(mapper.getClasseFrom())))
					.findFirst().orElse(null);
			Classe entity = entities.stream().filter(entita -> (entita.getNome().equals(mapper.getClasseTo())))
					.findFirst().orElse(null);

			if ("field".equals(stringCellValueTipoMapping) || "fieldMap".equals(stringCellValueTipoMapping)) {

				Mapping mapping = new Mapping(stringCellValueClasseModello, stringCellValueClasseEntity,
						stringCellValueTipoMapping);

				Campo campoModel = model.getListaCampi().stream()
						.filter(campo -> (nomeCampoModel.toString().equals(campo.getNome()))).findFirst().orElse(null);
				Campo campoEntity = entity.getListaCampi().stream()
						.filter(campo -> (nomeCampoEntity.toString().equals(campo.getNome()))).findFirst().orElse(null);

				AssociazioneCampi associazioneCampi = new AssociazioneCampi();
				associazioneCampi.setCampoFrom(campoModel);
				associazioneCampi.setCampoTo(campoEntity);
				mapper.getMappingEspliciti().add(mapping);
				mapping.setAssociazioneCampi(associazioneCampi);

			} else {
				if ("byDefault".equals(stringCellValueTipoMapping)) {
					mapper.setHasByDefault(true);
					
					for (Campo campoModel : model.getListaCampi()) {
						Campo mappingToField = entity.getListaCampi().stream()
								.filter(campo -> (campoModel.omonimous(campo))).findFirst().orElse(null);
						Mapping mapping = new Mapping(stringCellValueClasseModello, stringCellValueClasseEntity,
								stringCellValueTipoMapping);
						AssociazioneCampi associazioneCampi = new AssociazioneCampi();
						associazioneCampi.setCampoFrom(campoModel);
						associazioneCampi.setCampoTo(mappingToField);
						if (!Objects.isNull(mappingToField)) {
							mapper.getMappingImpliciti().add(mapping);
							mapping.setAssociazioneCampi(associazioneCampi);
						} else {
							mapper.getMappingMancantiFrom().add(mapping);
						}
					}
					List<AssociazioneCampi> mappingMancantiTo = entity.getListaCampi().stream()
							.filter(campo -> (model.getListaCampi().stream().noneMatch(campo::omonimous)))
							.map(campo -> {
								AssociazioneCampi ass = new AssociazioneCampi();
								ass.setCampoTo(campo);
								return ass;
							}).collect(Collectors.toList());
					for (AssociazioneCampi mappingMancante : mappingMancantiTo) {
						Mapping mapping = new Mapping(stringCellValueClasseModello, stringCellValueClasseEntity,
								stringCellValueTipoMapping);
						mapping.setAssociazioneCampi(mappingMancante);
						mapper.mappingMancantiTo.add(mapping);

					}
				}
				if ("customize".equals(stringCellValueTipoMapping)) {
					mapper.setHasCustom(true);
				}

			}
		}
	}

	// TODO: Crea funzione per scrivere il risultato dentro excel

	public String getNomeMapper() {
		return nomeMapper;
	}

	public void setNomeMapper(String nomeMapper) {
		this.nomeMapper = nomeMapper;
	}

	public String getFileMapper() {
		return fileMapper;
	}

	public void setFileMapper(String fileMapper) {
		this.fileMapper = fileMapper;
	}

	public String getClasseFrom() {
		return classeFrom;
	}

	public void setClasseFrom(String classeFrom) {
		this.classeFrom = classeFrom;
	}

	public String getClasseTo() {
		return classeTo;
	}

	public void setClasseTo(String classeTo) {
		this.classeTo = classeTo;
	}

	public List<Mapping> getMappingEspliciti() {
		return mappingEspliciti;
	}

	public void setMappingEspliciti(List<Mapping> mappingEspliciti) {
		this.mappingEspliciti = mappingEspliciti;
	}

	public List<Mapping> getMappingImpliciti() {
		return mappingImpliciti;
	}

	public void setMappingImpliciti(List<Mapping> mappingImpliciti) {
		this.mappingImpliciti = mappingImpliciti;
	}

	public List<Mapping> getMappingMancantiFrom() {
		return mappingMancantiFrom;
	}

	public void setMappingMancantiFrom(List<Mapping> mappingMancantiFrom) {
		this.mappingMancantiFrom = mappingMancantiFrom;
	}

	public List<Mapping> getMappingMancantiTo() {
		return mappingMancantiTo;
	}

	public void setMappingMancantiTo(List<Mapping> mappingMancantiTo) {
		this.mappingMancantiTo = mappingMancantiTo;
	}

	public boolean hasByDefault() {
		return hasByDefault;
	}

	public void setHasByDefault(boolean hasByDefault) {
		this.hasByDefault = hasByDefault;
	}

	public boolean hasCustom() {
		return hasCustom;
	}

	public void setHasCustom(boolean hasCustom) {
		this.hasCustom = hasCustom;
	}

}
