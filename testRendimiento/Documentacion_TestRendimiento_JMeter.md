# Documentación de Pruebas de Rendimiento con JMeter - Taller Testing 2025

Este documento detalla cómo configurar y ejecutar pruebas de rendimiento para el proyecto Taller Testing 2025 utilizando Apache JMeter.

## Índice
1. [Introducción](#introducción)
2. [Requisitos Previos](#requisitos-previos)
3. [Estructura del Plan de Pruebas](#estructura-del-plan-de-pruebas)
4. [Configuración](#configuración)
5. [Ejecución de las Pruebas](#ejecución-de-las-pruebas)
6. [Análisis de Resultados](#análisis-de-resultados)
7. [Personalización del Plan de Pruebas](#personalización-del-plan-de-pruebas)

## Introducción

Este documento presenta un plan de pruebas de rendimiento para el sistema Taller Testing 2025, utilizando la herramienta Apache JMeter. El objetivo es evaluar el rendimiento del sistema bajo diferentes cargas de usuarios y procesos, identificando posibles cuellos de botella y áreas de mejora.

## Requisitos Previos

Para ejecutar estas pruebas de rendimiento, necesitarás:

1. **Java JRE/JDK**: JMeter requiere Java 8 o superior.
2. **Apache JMeter**: Descargar la última versión desde [https://jmeter.apache.org/download_jmeter.cgi](https://jmeter.apache.org/download_jmeter.cgi). El archivo a descargar es **apache-jmeter-5.6.3.zip**
3. **Aplicación Taller Testing 2025**: El servidor backend debe estar ejecutándose localmente o en un entorno accesible.
4. **Base de datos**: La base de datos PostgreSQL debe estar configurada y accesible.

## Estructura del Plan de Pruebas

El plan de pruebas (`TestRendimiento_JMeter.jmx`) incluye:

1. **Grupo de Usuarios**: Configurado para simular 50 usuarios concurrentes con un tiempo de rampa de 30 segundos y un bucle de 10 iteraciones por usuario.
2. **Configuración HTTP**: Apuntando al servidor local en el puerto 8080.
3. **Escenarios de Prueba** (Sin autenticación):
   - **Lista de Marcas**: Obtener todas las marcas (/marca/mostrar).
   - **Lista de Modelos habilitados**: Obtener todos los modelos habilitados (/modelo/mostrarHabilitados).
   - **Lista de Clientes**: Obtener todos los clientes (/cliente/mostrar).
   - **Lista de Técnicos**: Obtener todos los técnicos (/tecnico/mostrar).
   - **Lista de Autos**: Obtener todos los autos (/auto/mostrar).
   - **Lista de Órdenes de Trabajo**: Obtener todas las órdenes de trabajo (/ordenTrabajo/mostrar).
   - **Creación de Cliente**: Crear un nuevo cliente con datos aleatorios (/cliente/guardar).
4. **Reportes de Resultados**: 
   - Ver Resultados en Tabla
   - Ver Árbol de Resultados
   - Informe Agregado
   - Gráfico de Resultados

## Configuración

### Paso 1: Instalar y configurar JMeter

1. Descargar JMeter desde [https://jmeter.apache.org/download_jmeter.cgi](https://jmeter.apache.org/download_jmeter.cgi)
2. Extraer el archivo ZIP descargado en una ubicación de tu elección
3. Iniciar JMeter:
   - En Windows: Ejecutar `bin/jmeter.bat`
   - En Linux/Mac: Ejecutar `bin/jmeter.sh`

### Paso 2: Cargar el archivo de prueba

1. Abrir JMeter
2. Seleccionar "Archivo" > "Abrir"
3. Navegar y seleccionar el archivo `TestRendimiento_JMeter.jmx` de este proyecto

### Paso 3: Ajustar la configuración (si es necesario)

Si la aplicación no se ejecuta en localhost:8080, deberás modificar:

1. En "Valores por Defecto HTTP":
   - Cambiar el dominio y puerto según corresponda

Nota: El plan de pruebas está configurado para ejecutarse sin autenticación. Si la aplicación requiere autenticación en el futuro, será necesario modificar el archivo JMX para agregar los pasos de autenticación y los encabezados de autorización correspondientes.

## Ejecución de las Pruebas

### Ejecución desde la interfaz gráfica

1. Verificar que tu aplicación Taller Testing 2025 esté en ejecución
2. En JMeter, hacer clic en el botón "Iniciar" (icono de triángulo verde) en la barra de herramientas
3. Observar los resultados en los diferentes visualizadores:
   - Ver Resultados en Tabla
   - Ver Árbol de Resultados
   - Informe Agregado
   - Gráfico de Resultados

### Ejecución desde línea de comandos (recomendado para pruebas intensivas)

```powershell
cd ruta\a\jmeter\bin
.\jmeter.bat -n -t "ruta\al\TestRendimiento_JMeter.jmx" -l resultados.jtl
```

Para generar un informe HTML después de la ejecución:

```powershell
.\jmeter.bat -g resultados.jtl -o "ruta\al\directorio\de\informe"
```

## Análisis de Resultados

Los resultados de las pruebas se pueden analizar mediante:

### Informe Agregado

Proporciona métricas clave:

- **Promedio**: Tiempo de respuesta promedio (ms)
- **Min/Max**: Tiempos de respuesta mínimo y máximo
- **Throughput**: Cantidad de solicitudes procesadas por segundo
- **Error %**: Porcentaje de errores
- **KB/sec**: Cantidad de datos transferidos por segundo

### Gráfico de Resultados

Muestra visualmente:

- **Tiempos de respuesta** a lo largo del tiempo
- **Throughput** (rendimiento) a lo largo del tiempo
- **Desviación estándar** en los tiempos de respuesta

### Ver Árbol de Resultados

Permite examinar detalladamente cada solicitud individual:

- **Código de respuesta HTTP** (200, 404, 500, etc.)
- **Datos de respuesta** devueltos por el servidor
- **Tiempos** de conexión y procesamiento

## Personalización del Plan de Pruebas

Para ajustar el plan de pruebas a tus necesidades específicas:

1. **Modificar número de usuarios**: Cambiar el valor de `ThreadGroup.num_threads` (por defecto: 50)
2. **Ajustar tiempo de rampa**: Cambiar el valor de `ThreadGroup.ramp_time` (por defecto: 30 segundos)
3. **Modificar número de iteraciones**: Cambiar el valor de `LoopController.loops` (por defecto: 10)
4. **Modificar endpoints**: Actualizar o agregar nuevos endpoints según sea necesario
5. **Agregar validaciones**: Añadir Assertions para verificar respuestas correctas
6. **Guardar resultados**: Configurar guardado de resultados para análisis posterior

### Ejemplo de personalización:

Para simular 100 usuarios con un tiempo de rampa de 60 segundos:

1. Seleccionar "Grupo de Usuarios"
2. Cambiar "Número de hilos" a 100
3. Cambiar "Tiempo de Rampa" a 60
4. Opcionalmente, cambiar el número de iteraciones (bucles)

## Consideraciones para la Interpretación de Resultados

Al analizar los resultados, tenga en cuenta:

1. **Tiempo de respuesta**: < 1 segundo suele ser aceptable para operaciones web normales
2. **Tasas de error**: Idealmente 0%, cualquier error debe ser investigado
3. **Throughput**: Cuanto mayor sea (para un tiempo de respuesta aceptable), mejor es el rendimiento
4. **Consistencia**: Poca variación en los tiempos de respuesta indica estabilidad
5. **Utilización de recursos**: Monitorear CPU, memoria y uso de red del servidor durante las pruebas

## Recomendaciones Finales

1. **Realizar pruebas en entornos similares a producción**
2. **Aislar la base de datos** para evitar afectar datos reales
3. **Incrementar gradualmente la carga** para identificar puntos de ruptura
4. **Comparar resultados entre versiones** para detectar regresiones de rendimiento
5. **Monitorizar recursos del servidor** durante las pruebas
6. **Analizar tiempos de respuesta por endpoint** para identificar los servicios más lentos que requieran optimización
7. **Revisar el tamaño de los datos retornados** para cada endpoint y considerar paginación donde sea apropiado
