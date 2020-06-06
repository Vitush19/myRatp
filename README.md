# MyRatp

MyRatp est une application mobile android qui permet à l'utilisateur de suivre en temps réel les horaires des différents transports (métros, bus, RER, tramways et noctiliens).
Elle repose sur l'api REST suivante pour récupérer les différentes lignes, stations, horaires : [api-ratp.pierre-grimaud.fr](https://api-ratp.pierre-grimaud.fr/v4/) et sur l'api REST suivante pour récupérer les logos des transports: [dataratp.opendatasoft.com](https://dataratp.opendatasoft.com/explore/dataset/pictogrammes-des-lignes-de-metro-rer-tramway-bus-et-noctilien/information/)

## Guide d'utilisation de l'application

Pour son utilisation, l'utilisateur doit disposer d'une connexion internet (WIFI ou 4G) afin d'accéder aux différentes fonctionnalités.
L'application dispose de 4 onglets : 
* Les Horaires : en sélectionnant cet onglet, il permet de choisir le mode transport, suivi de la ligne où il liste les différentes stations. L'utilisateur peut alors choisir de l'ajouter aux favoris ou bien de tous simplement accéder aux horaires de la station.
* Les Favoris : Cet onglet recense les différentes stations qui ont été ajoutées par l'utilisateur.
* Le lecteur de QR Code : Cet onglet permet à l'utilisateur de scanner le QR Code d'une station afin d'avoir rapidement accès à ses horaires.
* les informations traffic : Il recense les différentes informations concernant les différents transports que l'utilisateur pour être amené à emprunter.

## Fonctionnalités

* L'application dispose également d'une barre de recherche sur l'écran d'accueil qui permet à l'utilisateur d'accéder plus rapidement aux horaires en saisissant le nom d'une station de métro.
On retrouve également cette barre de recherche pour les lignes de bus, celles-ci étant assez nombreuses.
* L'utilisateur peut accéder après avoir un transport, une carte affichant les lignes via un boutton flottant.
* L'utilisateur peut glisser vers le bas la page affichant les horaires afin de les actualiser.
* L'utilisateur peut utiliser les QR Code pour accéder plus rapidement aux horaires.

