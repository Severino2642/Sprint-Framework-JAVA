# 🧩 Sprint Framework - ETU002642

Ce projet est un **framework Java basé sur Servlets**, conçu selon l'architecture **MVC**. Il permet de simplifier le développement web côté serveur en automatisant le routage, la gestion des vues, les requêtes HTTP, la validation de formulaire, les sessions et bien plus encore. Il s'inspire des grands frameworks comme Spring MVC, tout en gardant une structure légère et personnalisable.

## ⚙️ Configuration `web.xml`
```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <servlet>
        <servlet-name>FrontController</servlet-name>
        <servlet-class>mg.framework.controller.FrontController</servlet-class>
        <load-on-startup>1</load-on-startup>
        <init-param>
            <param-name>package_name</param-name>
            <param-value>controller</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>FrontController</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

## 🚀 Sprints Fonctionnels

### Sprint 0 - Initialisation
- Création d'un `FrontController` qui intercepte toutes les requêtes.
- Affiche simplement l'URL courante.

### Sprint 1 - Annotation @Controller
- Création de l'annotation `@Controller`.
- Scan du package contrôleur pour repérer les classes annotées.
- Affichage des noms des contrôleurs trouvés.

### Sprint 2 - Mapping des URLs avec @GET
- Création de l'annotation `@GET`.
- Ajout d’une classe `Mapping` (className, methodName).
- Scan des méthodes annotées `@GET` et création d’une `HashMap<String, Mapping>`.

### Sprint 3 - Exécution de Méthode Mappée
- Récupération de la classe et méthode depuis le mapping.
- Exécution dynamique via `Reflection`.

### Sprint 4 - Transmission vers la vue avec ModelView
- Création de la classe `ModelView` avec URL et data.
- Transfert des données avec `request.setAttribute()` puis `Dispatcher`.

### Sprint 5 - Gestion d'Exceptions
- Duplication d’annotations, contrôleurs absents, erreurs 404, types de retour non supportés.

### Sprint 6 - Envoi de données depuis un formulaire avec @RequestParam
- Annotation `@RequestParam` pour lier les données d’un formulaire aux paramètres de méthode.
- Intégration de la librairie `paranamer` pour récupérer les noms de paramètres à la compilation (`-g`).

### Sprint 7 - Passage d’un Objet Complet en Paramètre
- Annotation sur objet en paramètre.
- Parcours automatique des attributs via `request.getParameter()`.
- Annotation spécifique sur les attributs si les noms diffèrent.

### Sprint 8 - Gestion de Session avec MySession
- Création de la classe `MySession` (get, add, delete).
- Injection automatique si un paramètre est de type `MySession`.

### Sprint 9 - Support API REST avec @RestAPI
- Ajout d’annotation `@RestAPI` sur les méthodes.
- Conversion des données ou du `ModelView.data` en JSON.

### Sprint 10 - Gestion des Méthodes GET/POST
- Création des annotations `@POST` et `@URL`.
- Support de deux méthodes avec même URL mais verbes HTTP différents.
- Création de `VerbAction` pour identifier la méthode par verbe + URL.

### Sprint 11 - Gestion d’Exception via Web Page
- Affichage des messages d’erreurs dans la page HTML via `response.getWriter()`.

### Sprint 12 - Upload de Fichier
- Ajout de l’annotation `@MultipartConfig` sur `FrontController`.
- Support des types `Part` dans les paramètres de méthode.

### Sprint 13 & 14 - Validation de Formulaire
- Annotations : `@Required`, `@Numeric`, `@Mail`, `@Date`.
- En cas d’erreur, renvoi du message au-dessus de l’input concerné via `ValidationManager.error()`.
- Conservation des valeurs valides via `ValidationManager.value()`.

### Sprint 15 & 16 - Authentification
- Authentification par méthode ou par classe avec niveau requis.
- Comparaison entre niveau requis et niveau de session de l’utilisateur.
- Lève une exception si l’utilisateur ne dispose pas du niveau adéquat.

---

## 🧪 Technologies
- **Java Servlet**
- **JSP**
- **Reflection API**
- **Annotations Personnalisées**
- **JSON** (pour les API)
- **Paranamer**

---

## 🎯 Objectif

Proposer un framework léger, extensible, conçu pour l’apprentissage et les projets académiques. Il facilite la construction d’applications web en **respectant les bonnes pratiques MVC**, en fournissant des outils puissants de **routing, binding, validation, authentification et REST API**.
