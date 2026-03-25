README.txt - Practica de busqueda no informada

Demo.java es el punto de entrada principal para probar los
algoritmos de Hill Climbing y Simulated Annealing aplicados al problema
de rescate de grupos mediante helicópteros.

El programa se ejecuta a través de la consola y es totalmente interactivo,
guiando al usuario paso a paso para configurar el entorno del problema y los
parámetros del algoritmo.


CÓMO EJECUTAR EL PROGRAMA

Si utilizas un IDE (como Eclipse o IntelliJ), simplemente haz clic derecho
sobre la clase `Demo.java` y selecciona "Run as Java Application".

Si se hace mediante terminal, hay que seguir estos comandos:

1. Posicionarse en la carpeta src descompilada
2. Para compilar en funcion del sistema operativo:
    2.1. Windows: javac -cp ".;AIMA.jar;Desastres.jar" Solution/*.java
    2.2. Linux/Mac: javac -cp ".:AIMA.jar:Desastres.jar" Solution/*.java
3. Para ejecutar en funcion del sistema operativo:
    3.1. Windows: java -cp ".;*" Solution.Demo
    3.2. Linux/Mac: java -cp ".:*" Solution.Demo


PARÁMETROS DE CONFIGURACIÓN

Al iniciar, el programa te pedirá introducir secuencialmente los siguientes
valores por teclado:

1. Algoritmo a usar (HC / SA):
   - Escribe "HC" para usar Hill Climbing.
   - Escribe "SA" para usar Simulated Annealing.

2. Semilla:
   - Un número entero (ej. 1234) para generar el mapa. Usar la misma semilla
     garantiza que los centros y grupos aparezcan en las mismas coordenadas.

3. Numero de centros:
   - Cantidad de bases logísticas (ej. 5).

4. Numero de helicopteros por centro:
   - Cantidad de vehículos estacionados en cada base (ej. 1 o 2).

5. Numero de grupos a rescatar:
   - Cantidad de grupos esparcidos por el mapa (ej. 100).

6. Heuristica a usar (1 / 2):
   - 1: Minimiza el tiempo de rescate global (Eficiencia pura).
   - 2: Minimiza el tiempo global + penalización por urgencia médica (Prioridad 1).

7. Operador a usar (1 / 2 / 3):
   - 1: Mover (Pasa un grupo de un helicóptero a otro).
   - 2: Swap (Intercambia los grupos asignados a dos helicópteros).
   - 3: Invertir (Invierte el orden de rescate de un tramo de ruta).

8. Generador inicial (1 / 2 / 3):
   - 1: Secuencial (Asignación básica).
   - 2: Greedy (Busca el helicóptero más cercano).
   - 3: Random (Asignación puramente aleatoria).

PARÁMETROS EXCLUSIVOS DE SIMULATED ANNEALING

Si elegiste "SA" en el paso 1, el programa te pedirá 4 valores adicionales
para configurar el enfriamiento:
   - Steps: Iteraciones totales (ej. 10000).
   - Steps/iter: Iteraciones por cada bajada de temperatura (ej. 10).
   - K: Constante matemática de tolerancia inicial (ej. 10).
   - Lambda: Tasa de enfriamiento (ej. 1).
     *(Nota: En esta versión del demo, Lambda se introduce como un entero).*


OUTPUT

Una vez introducidos los datos, el programa imprimirá:

1. Estado Inicial: Lista de los centros generados, sus coordenadas y qué
   helicópteros están aparcados en cada uno.
2. Instrumentación: Datos internos del framework AIMA (como el número de
   nodos expandidos o iteraciones realizadas por el algoritmo).
3. Tiempo Total Final: El coste heurístico (en minutos) que ha logrado
   el algoritmo tras la optimización.
4. Asignación y Rutas: Un desglose final detallando qué helicóptero
   ha rescatado a qué grupo, y la ruta exacta ordenada que sigue cada
   helicóptero desde que sale de la base hasta que finaliza.


EJEMPLO DE EJECUCIÓN

Algoritmo a usar (HC / SA): HC
Semilla (para centros y grupos): 1234
Numero de centros: 5
Numero de helicopteros por centro: 1
Numero de grupos a rescatar: 100
Heuristica a usar (1 / 2): 1
Operador a usar (1: Mover, 2: Swap, 3: Invertir): 2
Generador inicial (1 / 2 / 3): 2

Estado Inicial Generado con exito.
[...]
Iniciando Hill Climbing...
[...]
>> TIEMPO TOTAL FINAL: 2597.57 minutos <<
--- Asignacion de Grupos a Helicopteros ---
[...]
--- Rutas completas por Helicoptero ---
[...]