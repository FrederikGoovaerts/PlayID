PlayID: a General Game Player using IDP
==================

This repository is created as for our project PlayID for the course "Wetenschappelijke Vorming" in KU Leuven. In this project we try to build a General Game Player that will translate the game specifications in GDL to FO(.) code and back to actions in GDL that are as optimal as possible. We will solve the FO(.) specification using the IDP3 system, a logic system created by the KRR group of KU Leuven.


To run PlayID, you have to give a GDL specification file as an argument to the PlayIdProcessor.java (located in fodot.communication).

You can generate the translations of all known GDL singleplayer games by running the SinglePlayerTransformationTest (location: fodot.tests.transformation) or can get the answer to all these games by running the SinglePlayerIdpParseTest.java (located in fodot.tests.idp).

Licensing
==================

PlayID is created by Frederik Goovaerts and Thomas Winters.
IDP is a system created by the KRR research group, part of DTAI of KU Leuven.

PlayID is licensed under the GNU GPL v3 (see LICENSE).
Licenses for all components used in this system can be found in the "licenses" subfolder.
