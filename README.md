# ToleranceMeasure

ToleranceMeasure is a simple console program in Java for analysing precision 
of sawing machines based on given measurements of products made by the machine.

**Note:**
It is my main project I use to consolidate my knowledge about Java and other
technologies. I will update this project as I gather more experience and knowledge.

## Features

1. Collecting data about products.
2. Collecting products measurements based on user input.
3. Saving last measurement sessions to text files, ready to print/analyse.
4. Creating directories and files based on product name, date and time.
5. Creating new products and saving them as presets in database.
6. Selecting products to use in measurement session.
7. Editing existing products (name, length and tolerances).
8. Deleting existing products.
9. Returning data about measurements and machine precision such as:

- Date and time of measurement session
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


## Usage

Program starts with main menu, in which user has to choose the command:

```bash
Main Menu
Enter one of the following commands: 
start - start the program and manually enter dimensions
select - select created product preset
manager - manage your products
exit - exit the program
```

### start

When user s 'start', the program will ask for needed product data:

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
Also, if that is the very first measurements session of that product, directory will be crated
with given product name.

```bash
Measurement session date: 2023/04/10 11:53:57

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

### select

When user enters 'select', list of created products will be printed.

```bash
Product ID: 1 Name: MyProduct1 Length: 60.0 Positive tolerance: -0.5 Negative tolerance: 0.0
Product ID: 2 Name: MyProduct2 Length: 120.0 Positive tolerance: -0.3 Negative tolerance: 0.6
Product ID: 3 Name: MyProduct3 Length: 95.0 Positive tolerance: -0.8 Negative tolerance: 0.0
Product ID: 4 Name: MyProduct4 Length: 54.0 Positive tolerance: -0.5 Negative tolerance: 0.5

Enter ID of the product to measure(-1 to go back):
```

Selecting ID of listed product will result in using its data for a measurement session.

However, if there are no products created, the program will 
inform about it and ask if new product should be created:

```bash
No products created.
Do you want to create new product? y/n
```

Entering 'n' will result in returning to main menu.
Entering 'y' will result in program asking for name, length and tolerances
and creating new product.

### manager

Manager is a sub menu that allows to create, edit and delete products:

```bash
Product manager
create - create new product
edit - edit existing product
delete - delete existing product
back - main menu
```

**Enjoy!**

## Author
*Mateusz Maciejak*
