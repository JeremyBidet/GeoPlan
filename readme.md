GeoPlan
=======

Introduction
------------

Ce projet AndroidStudio est une réponse au projet demandé dans le cadre de nos études en Master 2.
Le sujet du projet se trouve ici <http://igm.univ-mlv.fr/~chilowi/teaching/subjects/java/android/project/fapro/index.html>.

Description
-----------

Ce projet est une application Android permettant de créer, gérer, partager et CONTRÔLER des évènements. En effet lorsque un évènement est créé et qu'une liste de personne y est invité, l'organisateur à la possibilité de communiquer et localiser les participants de cet évènement afin de mieux gérer les points et horaires de rendez-vous. Les autres participants pourront également voir leur position en temps réel par rapport au lieu de l'évènement et envoyer très simplement des messages d'informations sur leur situation.

Dans des versions plus avancées nous aurons la possibilité de synchroniser les évènements avec ceux des grands systèmes de gestion déjà existants.

Voici les fonctionnalités à implémenter :

 * Un gestionnaire d'évènements
 * La géolocalisation des participants
 * Un système de transmission léger des coordonnées
 * Un radar et une carte de localisation
 * Un système d'envoi/réception de messages courts
 * Un daemon économique


Groupe
------

Nous sommes 6 à travailler sur ce projet.

### Équipe *Server*

 * Tristan FAUTREL
 * Jeremie CHATTOU

### Équipe *planning*

 * Huy HUYNH
 * Jeremy BIDET

### Équipe *Radar*

 * Maxime PICHOU
 * Pierre PERONNET

Fonctionnalités
---------------

### Planning

Utilisation d’un gestionnaire de planning/évènement (SEM=Schedule and Event Manager). Le SEM devra permettre de définir des évènements/rendez-vous, en prenant en compte les fonctionnalités de bases des solutions déjà existantes, ex :

 * Nom
 * Date et durée (date et heure début, date et heure fin)
 * Localisation
 * Participants (e-mail, ID user, etc… pour authentifier et joindre le participant)
 * Admin (voir Participants)
 * Description (détails)
 * Importance
 * Type (évènement, Rendez-vous, etc…)
 * Portée (personnel, groupe)
 * Partage (Publique, Privée)
 * Rappel (Temps avant début, répétitions, type (notifications, e-mail, etc…))
 * (Optionnel) Coût

D’autres options peuvent être déterminées.

### Géolocalisation

Lorsque l’application est démarrée et, uniquement, lorsqu’un évènement est commencé (ou sur le point de commencer), le service de géolocalisation se lance. Ce dernier utilisera les fonctionnalités natives d’Android, à savoir, le positionnement « fin » utilisant le GPS, et le positionnement mobile utilisant les réseaux mobiles (3G, 4G, etc…).

### Push position

Nous utiliserons un protocole simple et léger pour envoyer les données de position. Il est important de limiter la quantité des données transférée afin d’économiser les données mobiles et d’accélérer la transmission des données. Cette fonctionnalité permettra également de recevoir des positions externes.
Les participants, en utilisant l’application, acceptent de communiquer leur position avec les autres participant de l’évènement. Par défaut, seul l’organisateur de l’évènement reçoit la position des participants, cependant une fonctionnalité permettant de rendre sa position publique à tous les participants est intégrée. Une autre fonctionnalité permet aussi de faire une demande ponctuelle à n’importe quel participant, organisateur ou non. Cette demande doit être validée par le destinataire afin que le destinateur puisse accéder à sa position.

### Radar / Map

Il y aura deux moyens d’afficher les coordonnées échangées :

 * Le radar : ce dernier affiche le vecteur (distance et direction) de coordonnées entre la source et le receveur. Le receveur se situe toujours au milieu du radar, la source se situe au bout du vecteur. Ainsi nous obtenons de façon précise la distance et la direction entre la source et le receveur (tous les deux des participants). Ce radar est principalement utilisé pour se retrouver dans un lieu avec de l’affluence.
 * La carte (map) : cette fonctionnalité utilise la technologie Maps de Google pour afficher la position d’une personne. Elle est utile sur de longue distance.


### Push message

Cette fonctionnalité permet de communiquer à un ou plusieurs participants de courts messages. Elle propose par défaut des messages types tels que :

 * « Je suis en route »
 * « Je pars dans X minutes »
 * « J’arrive dans X minutes »
 * « Je suis coincé dans les bouchons »
 * « Il y a des problèmes de transports »
 * « Je serais en retard de X minutes »
 * Etc…

Le participant aura aussi la possibilité d’entrer un message personnalisé avec une limite de caractères. Cette limite est à déterminer dans les STD après avoir étudié les temps de transmissions de messages de différentes longueurs sur différents réseaux mobiles.
Par défaut il n’y a pas de restrictions sur l’échange de messages entre les participants. Potentiellement sur un broadcast réservé à l’organisateur.

### Deamon

« Last but not least » feature : le démon. Ce dernier permet de garder l’application en tâche de fond afin qu’elle check régulièrement les évènements et qu’elle active automatiquement les fonctionnalités de localisation et de messagerie lorsqu’un évènement démarre. Il devra être léger afin d’économiser l’utilisation de la batterie du mobile.

### TODO

 - Mise à jour de la position des utilisateurs lors de la réception d'un message serveur.
 - Tester le radar.
 - Mettre à jour la position de l'utilisateur avec [LocationListener](http://developer.android.com/reference/android/location/LocationListener.html).
