[![Java CI with Maven](https://github.com/kristiania-pgr209-2022/pg209exam-HauVu94/actions/workflows/maven.yml/badge.svg)](https://github.com/kristiania-pgr209-2022/pg209exam-HauVu94/actions/workflows/maven.yml)

Link to azure webapp: https://chatmessengerexam.azurewebsites.net/


# PG209 Backend programmering eksamen

## Sjekkliste for innleveringen

* [x] Dere har lest eksamensteksten
* [x] Koden er sjekket inn på github.com/pg209-2022 repository
* [x] Dere har lastet opp en ZIP-fil lastet ned fra Github
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub-konto (alternativt: README beskriver hvordan dere har jobbet)

## README.md

* [x] Inneholder link til Azure Websites deployment
* [x] Inneholder en korrekt badge til GitHub Actions
* [x] Beskriver hva dere har løst utover minimum
* [x] Inneholder et diagram over databasemodellen

## Koden

* [x] Oppfyller Java kodestandard med hensyn til indentering og navngiving
* [x] Er deployet korrekt til Azure Websites
* [x] Inneholder tester av HTTP og database-logikk
* [x] Bruker Flyway DB for å sette opp databasen
* [x] Skriver ut nyttige logmeldinger

## Basisfunksjonalitet

* [x] Kan velge hvilken bruker vi skal opptre som
* [x] Viser eksisterende meldinger til brukeren
* [x] Lar brukeren opprette en ny melding
* [x] Lar brukeren svare på meldinger
* [x] For A: Kan endre navn og annen informasjon om bruker
* [x] For A: Meldingslisten viser navnet på avsender og mottakere

## Kvalitet

* [x] Datamodellen er *normalisert* - dvs at for eksempel navnet på en meldingsavsender ligger i brukertallen, ikke i meldingstabellen
* [x] Når man henter informasjon fra flere tabellen brukes join, i stedet for 1-plus-N queries (et for hovedlisten og et per svar for tilleggsinformasjon)
* [x] Det finnes test for alle JAX-RS endpoints og alle DAO-er

## Ekstra funksjoner vi har lagt til
* [x]   Kunne endre informasjon om eksisterende bruker
* [x]  Brukeren ha felter utover navn og emailadresse
* [x]  Oversikten over meldingstråder skal vise per tråd navnet på alle mottakere for meldinger i tråden
* [x]  Forhåndspopulære med noen meldingstråder
* [x]  Mulighet til å opprette en meldingståd til flere mottakere
* [x]  Meldingen inneholde flere felter en tittel og meldingstekst
* [x]  Backend gjøre en join mellom melding- og brukertabellen for å vise avsenders navn
* [x]  Svaret må inneholde flere felter enn meldingstekst

## Hvordan vi har jobbet med eksamen
Vi har har alltid sittet sammen og jobbet med eksamen , og noen ganger jobbet på samme maskin a la parprogrammering
![DatabaseModel](https://user-images.githubusercontent.com/112695202/202850466-f3a621d2-169b-4bcf-887c-2d78bd953147.png)
 