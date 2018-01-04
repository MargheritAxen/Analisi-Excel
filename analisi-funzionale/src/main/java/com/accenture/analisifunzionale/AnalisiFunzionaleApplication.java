package com.accenture.analisifunzionale;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.accenture.analisifunzionale.classi.Classe;
import com.accenture.analisifunzionale.classi.Mapper;

@SpringBootApplication
@EnableAutoConfiguration
public class AnalisiFunzionaleApplication {

	public static void main(String[] args) throws IOException {

		String excelRisultatoCRA = "C:\\Users\\margherita.spadoni\\Desktop\\Analisi Excel\\Risultato.xlsx";
		String excelAttributiEntitiesCRA = "C:\\Users\\margherita.spadoni\\Desktop\\Analisi Excel\\cra\\Entity\\ListaAttributiEntities.xlsx";
		String excelAttributiModelCRA = "C:\\Users\\margherita.spadoni\\Desktop\\Analisi Excel\\cra\\Model\\ListaAttributiModel.xlsx";
		String excelAttributiEntitiesGL = "C:\\Users\\margherita.spadoni\\Desktop\\Analisi Excel\\gl\\Entity\\ListaAttributiEntity.xlsx";
		String excelAttributiModelGL = "C:\\Users\\margherita.spadoni\\Desktop\\Analisi Excel\\gl\\Model\\ListaAttributiModels.xlsx";
		
		System.out.println("----------------------------------------------------------------");
		System.out.println("Inizia il recupero dati in CRA");
		System.out.println("----------------------------------------------------------------");

		List<Classe> entities = Classe.recuperaClassi(excelAttributiEntitiesCRA, "Entities");
		Classe.recuperaCampiClassi(excelAttributiEntitiesCRA, "Entities", entities);
		for (Classe item : entities) {
			System.out.println(item.toString());
		}

		List<Classe> models = Classe.recuperaClassi(excelAttributiModelCRA, "Models");
		Classe.recuperaCampiClassi(excelAttributiModelCRA, "Models", models);
		for (Classe item : models) {
			System.out.println(item.toString());
		}

		System.out.println("----------------------------------------------------------------");
		System.out.println("Recupera i mapping impliciti Entity-Model di CRA");
		System.out.println("----------------------------------------------------------------");

		// Recupero i campi uguali tra Entity e Model e stampo a video le classi
		// coinvolte, trovo quindi quali
		// campi delle entità mappano con i rispettivi model -> CRA
		Classe.recuperaCampiClassi(excelAttributiEntitiesCRA, "Entities", entities);
		Classe.recuperaCampiClassi(excelAttributiModelCRA, "Models", models);
		for (Classe nomeCampoEntityCRA : entities) {
			for (Classe nomeCampoModelCRA : models) {
				if (nomeCampoEntityCRA.getListaCampi().toString()
						.equals(nomeCampoModelCRA.getListaCampi().toString())) {
					System.out.println("Il nome della classe Entity è: " + nomeCampoEntityCRA.getNome());
					System.out.println("Il nome della classe Model è: " + nomeCampoModelCRA.getNome());
					System.out.println("Il campo Entity in comune è: " + nomeCampoEntityCRA);
					System.out.println("Il campo Model in comune é: " + nomeCampoModelCRA);
				}
			}
		}

		System.out.println("----------------------------------------------------------------");
		System.out.println("Termina il recupero dati in CRA ed inzia il recupero dati in GL.");
		System.out.println("----------------------------------------------------------------");

		List<Classe> entity = Classe.recuperaClassi(excelAttributiEntitiesGL, "Entity");
		Classe.recuperaCampiClassi(excelAttributiEntitiesGL, "Entity", entity);
		for (Classe item : entity) {
			System.out.println(item.toString());
		}

		List<Classe> model = Classe.recuperaClassi(excelAttributiModelGL, "Model");
		Classe.recuperaCampiClassi(excelAttributiModelGL, "Model", model);
		for (Classe item : model) {
			System.out.println(item.toString());
		}

		System.out.println("----------------------------------------------------------------");
		System.out.println("Recupera i mapping impliciti Entity-Model di GL");
		System.out.println("----------------------------------------------------------------");

		// Recupero i campi uguali tra Entity e Model e stampo a video le classi
		// coinvolte, trovo quindi quali
		// campi delle entità mappano con i rispettivi model -> GL
		Classe.recuperaCampiClassi(excelAttributiEntitiesGL, "Entity", entity);
		Classe.recuperaCampiClassi(excelAttributiModelGL, "Model", model);
		for (Classe nomeCampoEntityGL : entity) {
			for (Classe nomeCampoModelGL : model) {
				if (nomeCampoEntityGL.getListaCampi().toString().equals(nomeCampoModelGL.getListaCampi().toString())) {
					System.out.println("Il nome della classe Entity è: " + nomeCampoEntityGL.getNome());
					System.out.println("Il nome della classe Model è: " + nomeCampoModelGL.getNome());
					System.out.println("Il campo Entity in comune è: " + nomeCampoEntityGL);
					System.out.println("Il campo Model in comune é: " + nomeCampoModelGL);
				}
			}
		}

		System.out.println("----------------------------------------------------------------");
		System.out.println("Termina il recupero dati anche in GL.");
		System.out.println("----------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------");

		System.out.println("Inizia il recupero di Mapper Entity-Model CRA");
		System.out.println("----------------------------------------------------------------");

		// Leggo i mapper dal file excel
		String mappersSheetName = "Mappers";
		try (FileInputStream inputStream = new FileInputStream(new File(excelRisultatoCRA));) {
			try (Workbook workbook = new XSSFWorkbook(inputStream);) {
				Sheet mappersSheet = workbook.getSheet(mappersSheetName);
				List<Mapper> mappers = Mapper.recuperaNomeFile(excelRisultatoCRA, mappersSheetName);

				for (Mapper mapper : mappers) {
					Mapper.popolaMapper(mappersSheet, mapper, models, entities);
				}

				for (Mapper item : mappers) {
					System.out.println(item.toString());
				}

			}
		}
		System.out.println("----------------------------------------------------------------");
		System.out.println("Termina il recupero Mapper Entity-Model CRA");
		System.out.println("----------------------------------------------------------------");
		System.out.println("----------------------------------------------------------------");
	}
}
