# Dokumentation: Ellen Halvardsson

### Beskrivning av projektet
Det här projektet är en applikation som ska hjälpa användaren att hålla koll på sina uppgifter. Användaren ska kunna lägga till uppgifter, eller "todos" och se en lista över alla uppgifter.

### Vad ni har gjort
Jag har använt mig av Kafka och mitt egna web API för att skicka och hämta data. Jag har använt mig av MySQL för att spara min data. Jag har använt mig av Spring för att få tillgång till dess inbyggda services och funktionalitet. Jag har skapat en GUI som min frontend som är kopplad till kommunikationen med web API, Kafka och MySQL.

## Arbetet och dess genomförande
Jag började med att sätta upp och konfigurera min Kafka broker. Jag skapade sedan mitt web API och skapade en producer och en consumer för att skicka data till mitt topic. Jag skapade sedan en Entity klass för att skapa POJOs och använde mig av lombok för att generera mina tabeller i MySQL och skapade en repository för att enkelt kunna spara och hämta data från min databas. Jag skapade sedan min frontend och kopplade den till programmets funktioner genom klassen 'AppService'.

### Vad som varit svårt
Till en början var det lite svårt att förstå hur Kafka fungerade och hur jag skulle använda mig av det, samt hur alla delar skulle användas tillsammans.
Det var även väldigt krångligt att strukturera upp mitt projekt med flera moduler i IntelliJ. Jag hade problem att få modulerna att fungera tillsammans och spenderade en hel del tid på att försöka lösa kopplingen mellan dessa. Tillslut gjorde jag ett projekt utan moduler för att ha tid att göra klart resten av projektet.

### Beskriv lite olika lösningar du gjort
Jag har använt mig av Spring för att automatisera en hel del av mitt arbete, som @Entity för att generera mina tabeller i MySQL. Jag har även använt mig av JPA Repository för att enkelt kunna spara och hämta data från min databas.

### Beskriv något som var besvärligt att få till
Det var väldigt krångligt att få mina moduler att fungera tillsammans. Jag hade problem med att få mina moduler att hitta varandra och spenderade en hel del tid på att försöka lösa detta.

### Beskriv om du fått byta lösning och varför i sådana fall
Jag har fått skapa mitt projekt på nytt flera gånger då jag inte lyckats få mina moduler att fungera tillsammans, oftast verkar det vara Maven som inte vill samarbeta med mig.

Jag fick även lite problem med att min Java Swing GUI inte uppdaterade sig korrekt och först trodde jag att det berodde på att ID för varje todo inte hämtas korrekt mellan GUI och databas, men det visade sig bero på att GUI:ns thread blev blockerad av den tidskrävande kommunikationen med Web API -> Kafka -> Databas. Jag löste detta genom att köra dessa anrop i en egen thread med hjälp av SwingWorker. 

## Slutsatser
Jag är nöjd med slutresultatet även om det finns mycket att vidareutveckla. 
Det har varit ett väldigt lärorikt projekt att genomföra och särskilt eftersom vi har haft mer tid på oss att utveckla projektet så har man hunnit testa sig fram och testa olika lösningar. Jag har fått bättre förståelse för SpringBoot och Kafka.

### Vad gick bra
Det flöt på ganska bra i början trots lite oklarhet över vad hela projektet skulle gå ut på och hur dataflödet skulle se ut. Skapandet av GUI flöt också på förvånansvärt bra i början.

### Vad gick dåligt
Jag hade problem med att få moduler att fungera tillsammans och fick skapa om projektet flera gånger, men gjorde tillslut ett projekt utan moduler. Jag hade även problem med att få min GUI att uppdateras korrekt och fick lösa det genom att utföra mer tidskrävande anrop (all kommunikation som går genom Kafka) i en egen thread.

### Vad har du lärt dig
Jag har lärt mig en hel del om Kafka, SpringBoot, MySQL och GUI-design. Det var även väldigt lärorikt att få testa på att arbeta med flera moduler i IntelliJ, även om det inte gick så bra för mig. Det var även lärorikt att testa på MySQL då jag bara använt SQLite tidigare, och väldigt smidigt med JPA Repository!

### Vad hade ni gjort annorlunda om ni gjort om projektet
Jag hade velat få till det med moduler i IntelliJ så att jag kunde köra mitt program från en enda Main-klass i en Applikationsmodul. Men det hade krävt mer tid och jag kan såklart istället testa det på egen hand i framtiden.

### Vilka möjligheter ser du med de kunskaper du fått under kursen.
Jag tror att jag kommer ha nytta av de kunskaper jag fått under kursen i framtiden. Nu ser jag fram emot att bredda kunskaperna och bli mer säker i användningen av dom! 