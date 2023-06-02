# Documentation utilisateur

Ce document est un guide d'utilisation pour l'application Android que nous avons créée dans le cadre du cours de Matériels Mobile.

## Sommaire
1.  Connexion
2. Inscription
3. Accueil
4. Fiche d'un film
5. Commentaires d'un film
6. Compte utilisateur
7. Favoris
8. Recherche
9. QR Code

## Connexion

Lors du lancement de l'application, l'utilisateur est envoyé vers une page de connexion :

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/connexion.jpg" alt="Texte alternatif de l'image" />
</p>

Pour se connecter, l'utilisateur doit rentrer son pseudo et son mot de passe et appuyer sur **connexion**. S'il n'a pas de compte il doit s'inscrire en appuyant sur le bouton **inscription** pour être renvoyé vers la page correspondante. 
Si un utilisateur essaye de se connecter mais que son identifiant n'est pas reconnu, ou que son mot de passe ne correspond pas, un message d'erreur s'affiche.

## Inscription

Lorsque l'utilisateur appuie sur le bouton inscription, il est renvoyé vers cette page : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/inscription.jpg" alt="Texte alternatif de l'image" />
</p>

Il doit entrer ses informations personnelles tel que son nom, son prénom, son pseudo (par lequel il sera identifié dans l'application) et son mot de passe. Pour que l'utilisateur puisse s'inscrire, son pseudo doit être unique et tous les champs doivent être remplis. Si ce n'est pas la cas, un message d'erreur s'affiche. Si les informations inscrites par l'utilisateur sont correctes, en appuyant sur **créer mon compte**, il sera renvoyé vers la page de connexion.

Les utilisateurs sont stockés localement, chaque téléphone a donc une liste d'utilisateur propre et les utilisateurs peuvent seulement interragir entre eux sur un même appareil. Une amélioration possible de notre application serait donc de la relier à une base de données distante.

## Accueil

On arrive sur cette page lorsqu'on se connecte. L'utilisateur peut y trouver différents types de films susceptibles de lui plaire :
* les films populaires en ce moment,
* les films les mieux notés,
* les derniers films français.

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/accueil.jpg" alt="Texte alternatif de l'image" />
</p>

C'est à partir de cette page qu'on peut accéder à l'ensemble des fonctionnalités de l'application : 
* Lorsqu'on clique sur une carte de film : <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/carte.jpg" alt="Texte alternatif de l'image" /> l'utilisateur est redirigé vers une page contenant des informations plus détaillées sur le film.
* Lorsqu'on clique sur l'icone <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/icone_profil.jpg" alt="Texte alternatif de l'image" />, située dans la barre de menu, l'utilisateur est redirigé vers sa page de profil.
* Lorsqu'on clique sur l'icone <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/icone_favoris.jpg" alt="Texte alternatif de l'image" />, située dans la barre de menu, l'utilisateur est redirigé vers sa page de favoris.
* Lorsqu'on clique sur **Rechercher un film**, accessible via <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/points.jpg" alt="Texte alternatif de l'image" />, dans la barre de menu, l'utilisateur est redirigé vers la page de recherche.
* Lorsqu'on clique sur **Scanner un QR Code**, accessible via <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/points.jpg" alt="Texte alternatif de l'image" />, dans la barre de menu, l'utilisateur est redirigé vers la page du scanner.

## Fiche d'un film

Cette page est accessible quand on clique sur la carte d'un film : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/fiche_1.jpg" alt="Texte alternatif de l'image" />
</p>

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/fiche_2.jpg" alt="Texte alternatif de l'image" />
</p>

L'utilisateur peut alors visualiser le résumé du film, les acteurs, la bande annonce si elle existe, les recommandations, …

Pour ajouter un film à ses favoris, l'utilisateur peut cliquer sur l'étoile : <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/bouton_éteint.jpg" alt="Texte alternatif de l'image" />. L'étoile s'allume alors  : <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/bouton_allumé.jpg" alt="Texte alternatif de l'image" /> et l'utilisateur reçoit un message lui indiquant que le film a bien été ajouté. Inversement, il suffit d'appuyer à nouveau sur l'étoile pour retirer le film de ses favoris.

Lorsque l'utilisateur appuie sur l'icone <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/icone_comments.jpg" alt="Texte alternatif de l'image" />, située sur la barre de menu, il est redirigé vers la page de commentaires associée au film.

Lorsque l'utilisateur appuie sur l'icone <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/icone_share.jpg" alt="Texte alternatif de l'image" />, située en haut de la barre de menu, il a la possibilité de partager un message par rapport au film à l'un de ses contacts via mail, SMS, … 

## Commentaires d'un film

Cette page est accessible depuis la fiche du film. Elle permet à l'utilisateur de laisser un commentaire sur le film en question : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/commentaires.jpg" alt="Texte alternatif de l'image" />
</p>

Pour laisser un commentaire, l'utilisateur écrit le commentaire qu'il souhaite poster dans la zone dédiée, puis il doit appuyer sur le bouton **ajouter un commentaire**. Les commentaires s'affichent du plus ancien au plus récent.

## Compte utilisateur

Cette page est accessible depuis la page d'accueil : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/profil.jpg" alt="Texte alternatif de l'image" />
</p>

L'utilisateur peut visualiser ses informations et les mettre à jour s'il le souhaite en modifiant l'un des champs de texte, puis en appuyant sur le bouton **mettre à jour les informations**. L'utilisateur peut également visualiser la liste de ses films favoris.

Lorsque l'utilisateur appuie sur l'icone <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/icone_déconnecter.jpg" alt="Texte alternatif de l'image" />, il est déconnecté et est donc renvoyé sur la page de connexion.

## Favoris

Cette page est accessible depuis la page d'accueil et permet à l'utilisateur de visualiser la liste de ses films favoris : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/favoris.jpg" alt="Texte alternatif de l'image" />
</p>

Le bouton <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/icone_sync.jpg" alt="Texte alternatif de l'image" /> permet de mettre à jour la liste des favoris (même si elle se fait automatiquement, il y a parfois quelques exceptions quand on fait un retour en arrière).

## Recherche

Cette page est accessible depuis la page d'accueil. Elle permet à l'utilisateur de rechercher un film : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/recherche.jpg" alt="Texte alternatif de l'image" />
</p>

Pour lancer une recherche, l'utilisateur a simplement à saisir ce qu'il recherche dans le champ dédié et appuyer sur la touche **OK** du clavier.

Il est possible de trier les résultats de la recherche en appuyant sur : <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/spinner.jpg" alt="Texte alternatif de l'image" />. Une boîte de dialogue s'ouvre alors et l'utilisateur peut choisir comment il veut trier les résultats : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/tri.jpg" alt="Texte alternatif de l'image" />
</p>

Il est également possible de filtrer les résultats de la recherche : 
* pour les genres, on utilise la liste : <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/liste.jpg" alt="Texte alternatif de l'image" />. Elle contient l'ensembles des genres présents dans les résultats de le recherche et sont tous activés par défaut. si l'utilisateur ne veut pas les films d'un certain genre, il lui suffit de cliquer sur le genre en question, qui se désactive alors : <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/liste_désactivée.jpg" alt="Texte alternatif de l'image" />, et les résultats sont ainsi mis à jour.
* pour les dates et les notes, l'utilisateur doit cliquer sur l'icone  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/icone_filtre.jpg" alt="Texte alternatif de l'image" />. Un boîte de dialogue s'ouvre alors et l'utilisateur peut inscrire les paramètres qu'il souhaite : 

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/filtre.jpg" alt="Texte alternatif de l'image" />
</p>

## QR Code

Cette page est accessible depuis l'accueil, elle permet de scanner un QR Code contenant l'identifiant d'un film. Quand la page s'ouvre, une autorisation est demandée pour accéder à la caméra de l'appareil  :

<p align="center">
  <img src="https://raw.githubusercontent.com/AurelieVidal/projet_Android/main/images/autorisation.jpg" alt="Texte alternatif de l'image" />
</p>

Si l'utilisateur refuse, il est redirigé vers la page d'accueil. S'il accepte, il peut scanner un QR Code. Si le QR Code est valide et renvoie bien à un film, alors la fiche du film apparaît. En revanche, si le QR Code n'est pas pas valide, l'utilisateur sera redirigé vers la page d'accueil avec un message d'erreur.
