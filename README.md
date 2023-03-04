# ToleranceMeasure

ToleranceMeasure is a simple console program in Java for analysing precision of machines based on collection of measured products.

## Features

1. Collecting data about source product and measurement tolerance
2. Collecting products measurements based on user input
3. Validation of given information
4. Returning data about measurements and machine precision such as:

```bash
For product length 60mm and tolerance between -0,5mm to +0,0mm:

All measurements(mm): [59.6, 59.8, 59.9, 60.0, 59.5, 59.3, 59.4, 59.7, 60.0, 60.1, 59.6, 59.8, 60.2, 60.0, 59.6]
Amount measured: 15 pcs
Average of all measurements: 59,77mm

TOLERANCE DATA
Outside tolerance: 4 pcs
Inside tolerance: 11 pcs
Bigger than 60.0mm: 2 pcs
Smaller than 59.5mm: 2 pcs

The biggest measurement: 60.2mm
The smallest measurement: 59.3mm
The difference between the smallest and the biggest measurement: 0,9mm

Measurements sorted from min to max(mm): [59.3, 59.4, 59.5, 59.6, 59.6, 59.6, 59.7, 59.8, 59.8, 59.9, 60.0, 60.0, 60.0, 60.1, 60.2]
```

## Usage

Program first asks you to enter product length:

```bash
Enter product length (mm): 60
```

Then you should provide positive tolerance:

```bash
Enter positive tolerance (e.g. 0,5): 0
```

Finally, enter negative tolerance

```bash
Enter negative tolerance (e.g. -0,5): -0,5
```

Now, enter your measurements of products. You can enter how many you want.
**Be careful, you can't delete entered measurement!**

```bash
Enter the measurements, enter '0' after the last measurement to confirm: 
59,6
59,8
59,9
60
59,5
59,3
59,4
59,7
60
60,1
59,6
59,8
60,2
60
59,6
0
```

After entering 0, the program will print data about your inputs.
It will also ask if you want to measure something else. Simply choose y/n:

```bash
Do you want to restart? Enter y/n:
```

**Enjoy!**

## Author
*Mateusz Maciejak*