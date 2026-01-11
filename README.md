# PLAYER MUSIC
Esta app es un reproductor de música local, obtiene la música guardada de la memoria 
interna y SD del dispositivo, ofrece la opción de agregar y eliminar música de una 
playlist, así como crear o quitar una playlist, puede reproducir música en aleatorio, 
o en secuencia.

# Características principales
- 🪟 Interfaz moderna con Jetpack Compose
- 🌐 Navegación con Navigation Component
- 📊 Integración con Service, ViewModel + StateFlow
- 🎨 Patrón de diseño arquitectónico con MVVM + Hexagonal
- 🧩 Inyección de dependencias con Hilt
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
- Al abrir la app, se muestra la vista de inicio *Lista de Artistas*
- Desde *Lista de Artistas* puede hacer click en la barra superior a la derecha donde hay un icono *Listas de Reproduccion*
- Desde *Lista de Artistas* puede hacer click en algun artista, esto le llevara a la *Musica del Artista*
- Desde *Listas de Reproduccion* puede modificar el nombre de cada lista o borrarlas, si hace click en una lista se abrira *Lista de Reproducion*
- Desde *Lista de Reproducion* puede hacer click en una musica para reproducir *Musica Actual* o puede hacer click en el icono de borrar a la derecha para quitarla de la lista.
- Desde *Musica del Artista* puede hacer click en una musica para reproducir *Musica Actual* o puede hacer click en el icono de agregar a la derecha para añadir una *Lista de Reproducion*
- Para agregar la musica a una *Lista de Reproducion* se abre una vista "Agregar listas" donde se puede escribir un nombre para la nueva lista y luego se hace click en la derecha para crear la *Lista de Reproducion* y a la vez agregarle dicha musica, tambien puedes en la misma vista "Agregar listas" buscar una *Lista de Reproducion* existente para agregar hay la musica.
- Desde *Musica Actual* puede ir a *Lista de Reproducion* o configurar el aleatorio o repetir musica, asi como cambiar a la siguiente o anterior musica, asi como pausar o reanudar la reproducion.

# Ver video Demo
[Ver en YouTube](https://youtu.be/8OROZRqCzdU?si=VAOE7lX58A5ScVyq)

# Contribución
- Haz un fork del repositorio
- Crea una rama con tu feature: git checkout -b feature/nueva-funcionalidad
- Haz commit de tus cambios: git commit -m "Agrega nueva funcionalidad"
- Haz push a la rama: git push origin feature/nueva-funcionalidad
- Abre un Pull Request

# Licencia
Este proyecto está bajo la licencia GPL-3.0. Consulta el archivo LICENSE para más detalles.
