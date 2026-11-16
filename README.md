# 📊 Analisi-Excel — Automazione del Data Mapping & Analisi Funzionale

[![Java](https://img.shields.io/badge/Java-8-ED8B00?logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-1.4.7-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Apache POI](https://img.shields.io/badge/Apache%20POI-3.17-D22128?logo=apache&logoColor=white)](https://poi.apache.org/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)

Applicazione **Java / Spring Boot** sviluppata per automatizzare le attività di **analisi funzionale e data mapping** tra modelli dati complessi (confronto programmatico tra classi **Entities** di persistenza e **Models / DTO** di business).

---

## 🎯 Obiettivo del Progetto

Nelle integrazioni di sistemi enterprise (es. moduli **CRA** e **GL**), il tracciamento e la riconciliazione manuale di centinaia di classi e dei rispettivi attributi su fogli Excel è un processo lungo e suscettibile a errori umani.

Questo applicativo automatizza l'intero ciclo:
1. **Importa ed elabora fogli Excel** contenenti le specifiche di entità e modelli.
2. **Riconosce i mapping impliciti** (attributi omologhi per nome, tipo o semantica tra Entity e Model).
3. **Genera il report consolidato dei mapping** esportando un foglio Excel di sintesi (`Risultato.xlsx`) utilizzabile sia per la documentazione funzionale che per la configurazione dei mapper di codice (Orika Mapper).

---

## ⚙️ Funzionalità Chiave

* **Lettura e Parsing XLSX (Apache POI):** Caricamento programmatico degli schemi applicativi (`ListaAttributiEntities.xlsx`, `ListaAttributiModel.xlsx`).
* **Modellazione ad Oggetti:** Astrazione di concetti funzionali tramite classi dedicate:
  * `Classe`: Rappresentazione della classe (Entity/Model) e del suo namespace.
  * `Campo`: Modellazione del singolo attributo (nome, tipo di dato, visibilità).
  * `Mapping`: Definizione della corrispondenza tra entità sorgente e modello target.
  * `AssociazioneCampi`: Vincolo specifico tra campi per la trasformazione dati.
* **Algoritmo di Confronto e Riconciliazione:** Scansione incrociata automatica dei campi tra liste di classi per rilevare corrispondenze senza bisogno di query manuali.
* **Integrazione Orika Mapper:** Supporto a librerie di mapping dinamico per l'esecuzione a runtime delle trasformazioni tra DTO ed Entity.

---

## 🛠️ Stack Tecnologico

| Tecnologia | Versione | Scopo |
| :--- | :--- | :--- |
| **Java** | 1.8 (OpenJDK / Oracle JDK) | Linguaggio di sviluppo core |
| **Spring Boot** | 1.4.7.RELEASE | Framework per runtime, dependency injection e configurazione |
| **Apache POI / POI-OOXML** | 3.17 | Lettura e scrittura nativa di file Excel Microsoft (`.xlsx`) |
| **Orika Core** | 1.4.6 | Libreria per Java Bean mapping performante |
| **Hibernate / Spring Data JPA** | Inclusa | Supporto per la persistenza su database relazionali (MySQL) |
| **Maven** | 3.x | Build automation e gestione dipendenze |

---

## 📂 Struttura del Progetto

```
Analisi-Excel/
└── analisi-funzionale/
    ├── pom.xml                                   # Configurazione dipendenze Maven
    └── src/
        └── main/
            └── java/
                └── com/accenture/analisifunzionale/
                    ├── AnalisiFunzionaleApplication.java  # Main runner e orchestratore dell'analisi
                    └── classi/
                        ├── Classe.java           # Parser Excel per l'estrazione delle classi
                        ├── Campo.java            # Rappresentazione del singolo attributo
                        ├── Mapping.java          # Relazione tra classi sorgente/destinazione
                        ├── AssociazioneCampi.java# Corrispondenza specifica campo-campo
                        ├── Mapper.java           # Gestione logica mapper
                        └── liste/
                            └── ListaAttributiClasse.java # Gestione collezioni attributi
```

---

## 🚀 Istruzioni di Avvio

### Prerequisiti
* Java JDK 8 o superiore installato (`java -version`).
* Maven installato (`mvn -version`) oppure utilizzo del wrapper `mvnw` incluso.

### Compilazione ed Esecuzione
1. **Entra nella directory del modulo:**
   ```bash
   cd analisi-funzionale
   ```
2. **Compila il progetto con Maven:**
   ```bash
   ./mvnw clean install
   ```
3. **Avvia l'applicazione Spring Boot:**
   ```bash
   ./mvnw spring-boot:run
   ```
   L'applicativo eseguirà il parsing dei file Excel di input configurati e stamperà su console/file il resoconto completo dei mapping rilevati.
