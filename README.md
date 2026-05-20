# PLAYER MUSIC
PLAYER MUSIC es una aplicación móvil nativa de Android diseñada para ofrecer una experiencia de reproducción de música local fluida y organizada. El objetivo principal es permitir a los usuarios gestionar y disfrutar de su biblioteca musical personal almacenada en el dispositivo, con funcionalidades robustas para la creación y administración de playlists.

# Características principales
- 🪟 Interfaz moderna con Jetpack Compose
- 🌐 Navegación con Navigation Component
- 📊 Integración con Service, ViewModel + StateFlow
- 🎨 Patrón de diseño arquitectónico con MVVM + Clean Architecture
- 💉 Inyección de dependencias con Hilt
- 💽 Base de datos local con Room
- 📱 Compatible con Android 7.0 (API 24) en adelante

# Instalación
- Clona el repositorio: git clone https://github.com/yjot-dev/Rep-PlayerMusic.git
- Abre el proyecto en Android Studio (Giraffe o superior)
- Sincroniza dependencias con Gradle
- Conecta un dispositivo o emulador y ejecuta la app

# Tecnologías usadas
- Kotlin
- Jetpack Compose
- AndroidX (Navigation, Lifecycle, Core KTX)
- Material 3

# Uso
El flujo de uso de la aplicación está diseñado para ser intuitivo y potente, guiando al usuario a través de los siguientes pasos:
 
1. Exploración de la Biblioteca Musical: Al iniciar la aplicación, el usuario es recibido en la vista principal, la Lista de Artistas. Esta pantalla organiza automáticamente toda la música local del dispositivo por artista, sirviendo como el punto de partida para la exploración musical. Desde aquí, el usuario puede:
   - Seleccionar un artista para navegar a una vista detallada con todas sus canciones.
   - Acceder a la gestión de playlists a través de un ícono en la barra superior.
2. Gestión y Creación de Playlists: La aplicación ofrece un sistema completo para que los usuarios guarden sus propias colecciones de música:
   - Vista de Listas de Reproducción: Desde aquí, los usuarios pueden ver todas sus playlists creadas. Tienen la capacidad de editar el nombre de cualquier lista o eliminarla por completo.
   - Añadir Música a una Playlist: Mientras explora la música de un artista, el usuario puede seleccionar una canción y añadirla a una playlist. Se presenta una interfaz donde puede buscar una lista existente o crear una nueva al instante. Al crear una nueva lista, la canción se añade automáticamente a ella, agilizando el proceso.
   - Gestionar Contenido de una Playlist: Al entrar en una playlist específica, el usuario puede ver todas las canciones que contiene y tiene la opción de eliminar pistas individualmente.
3. Reproducción de Música: El núcleo de la experiencia se centra en la pantalla de Música Actual, el reproductor principal. El usuario puede llegar aquí seleccionando una canción desde la lista de un artista o desde una de sus playlists. Esta pantalla proporciona control total sobre la reproducción con las siguientes funcionalidades:
   - Controles Esenciales: Pausar, reanudar, y navegar a la canción siguiente o anterior en la cola de reproducción.
   - Modos de Reproducción: El usuario puede personalizar su experiencia auditiva activando el modo de reproducción aleatoria (shuffle) o configurando diferentes modos de repetición (repetir toda la lista o la canción actual).

En resumen, PLAYER MUSIC empodera al usuario con una herramienta completa para organizar y disfrutar de su música local, permitiendo una gestión de biblioteca y una experiencia de reproducción eficientes y agradables.

# Ver video Demo
[Ver en YouTube](https://youtu.be/Ivs9VpvraVI)

# Contribución
- Haz un fork del repositorio
- Crea una rama con tu feature: git checkout -b feature/nueva-funcionalidad
- Haz commit de tus cambios: git commit -m "Agrega nueva funcionalidad"
- Haz push a la rama: git push origin feature/nueva-funcionalidad
- Abre un Pull Request

# Licencia
Este proyecto está bajo la licencia GPL-3.0. Consulta el archivo LICENSE para más detalles.
