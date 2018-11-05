# Bildverarbeitung Projekt 2: Kontrastanpassung

Verantwortlich: [Torsten Rauch](https://github.com/ToRauch), [Julian Knaup](https://github.com/julianknaup), [Alissa Müller](https://github.com/chaosbambi) & [Jan-Philipp Töberg](https://github.com/Janfiderheld)

Projektbetreuer: Dipl.-Inform. Jan Leif Hoffmann & Prof. Dr.-Ing. Volker Lohweg 


Startdatum: 31.10.2018

Enddatum & Vorstellung: 14.11.2018

## Aufgabenbeschreibung:

### Programmierung

- Erstellen Sie ein Java-Plugin für ImageJ, das über einen Nutzerdialog beliebige minimale und maximale 8-Bit-Referenzgrauwerte einliest und ein Ausgangsbild auf diese Werte linear skaliert 
- Verwenden Sie als Testeingabe die gegebenen Bilder und u.a. die Werte 0/255 (min/max).
- Welche Erkenntnisse können Sie durch geeignete Kontrastanpassungen gewinnen bzgl.
  - enhance-me.gif,
  - dem gruppenspezifischen Bild und
  - pluto.png?
- Implementieren Sie eine Automatik, die die Skalierung so auswählt, dass mindestens 1 % (x %) der Pixel schwarz (0) bzw. weiß (255) wird

### Ausarbeitung & Präsentation

- Präsentieren Sie (d.h. als Gruppe) Ihr Ergebnis, insbesondere unter erläuternder Darstellung der notwendigen Gleichungen und der Verwendung geeigneter Grafiken (8 min)
- Fertigen Sie individuell (d.h. pro Person) eine zwei Seiten umfassende wissenschaftliche Ausarbeitung unter Nutzung der LaTeX-Vorlage an

### Anmerkungen

- Hinweis: Parameter und Rückgabewert – siehe Dokumentation
- IJ.getNumber() öffnet ein Dialogfenster zum Einlesen nutzerspezifizierter Werte 
- Textausgabe
  - IJ.log() ermöglicht eine Log-Ausgabe in einem Textfenster
  - Allgemeinere Alternative: Klasse ij.text.TextWindow
  - Popup-Dialog (sog. Alert): IJ. showMessage()

