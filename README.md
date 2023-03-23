# ToleranceMeasure

ToleranceMeasure is a simple console program in Java for analysing precision of sawing machines based on given measurements of products made by the machine.

## Features

1. Collecting data about the product and tolerances
2. Collecting products measurements based on user input
3. Validation of user input
4. Returning data about measurements and machine precision such as:

- List of all measurements
- Amount measured
- Average of all measurements
- Products outside tolerance
- Products inside tolerance
- Products bigger than positive tolerance margin
- Products smaller than negative tolerance margin
- Biggest measurement
- Smallest measurement
- Difference between the smallest and the biggest measurement 
- Measurements sorted ascending

5. Saving last measurement session to text file, ready to print/analyse.


## Usage

Program starts with main menu, in which user has to choose the command:

```bash
Main Menu
Enter one of the following commands: 
start - to start the program
last - to show data from last measurement session
exit - to exit the program
```

When user enter 'start', the program will ask for needed product data:

```bash
Enter product name: MyProduct
Enter product length: 60
Enter positive tolerance: 0
Enter negative tolerance: -0.5
```

Program checks on every step, if given input is correct. For example, user can't
enter positive tolerance that is below zero. When sum of positive and negative tolerance
equals 0, it will also ask to enter correct tolerances.

Now, user can enter how many products measurements he wants. If user made a mistake, he
can simply enter '-1' to delete last measurement. The algorithm here checks many different
things to prevent bugs or throwing exceptions.

```bash
Enter products measurements.
0 - to confirm your measurements
-1 - to delete last measurement (undo)
59.8
59.6
59.4
59.8
59,9
Wrong measurement, enter correct: 59.9
59.5
59,3
Wrong measurement, enter correct: 59.5
59.9
60
60.1
59.6
59.8
59.9
60
60.2
60
59.6
59.8
59.7
59.5
59.6
60.2
60
59.8
0
```
When 0 is entered, the program will ask for confirmation:

```bash
Current measurements: [59.8, 59.6, 59.4, 59.8, 59.9, 60, 59.5, 59.5, 59.9, 60, 60.1, 59.6, 59.8, 59.9, 60, 60.2, 60, 59.6, 59.8, 59.7, 59.5, 59.6, 60.2, 60, 59.8]
Do you want to confirm the measurements? (y/n): y
```

After entering 'y', the program will print data about precision and save it into file.

```bash
All measurements(mm): [59.8, 59.6, 59.4, 59.8, 59.9, 60, 59.5, 59.5, 59.9, 60, 60.1, 59.6, 59.8, 59.9, 60, 60.2, 60, 59.6, 59.8, 59.7, 59.5, 59.6, 60.2, 60, 59.8]

Amount measured: 25 piece(s)
Average of all measurements: 59.81mm

TOLERANCE DATA
Outside tolerance: 4 piece(s)
Inside tolerance: 21 piece(s)
Bigger than 60mm: 3 piece(s)
Smaller than 59.5mm: 1 piece(s)

Biggest measurement: 60.2mm
Smallest measurement: 59.4mm
The difference between the smallest and the biggest measurement: 0.8mm

Measurements sorted ascending: [59.4, 59.5, 59.5, 59.5, 59.6, 59.6, 59.6, 59.6, 59.7, 59.8, 59.8, 59.8, 59.8, 59.8, 59.9, 59.9, 59.9, 60, 60, 60, 60, 60, 60.1, 60.2, 60.2]
```

**Enjoy!**

## Author
*Mateusz Maciejak*
