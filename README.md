## Welcome to my solution to Google Hash Code 2021 

Here is my personal solution to google hash code 2021
I was able to score 9,555,584 with this solution but it was during the extended round not the main round.

### More details about the solution

It's good to note that the solution was not focused about the single responsibility or applying perfect OOP as all of the class members in this solution are public which is not recommended in OOP. But it makes it faster / easier to implement what i want and make me put my focus on the algorithm and solving the problem.

Also it's good to note that i didn't separate parsing the input. I filled the data in multiple different objects when parsing the input (i.e editing streets and intersections directly when parsing cars). This way i could go over the data one time only when scanning and not loop over them to collect whatever data i needed. This made my solution very fast without any non required complexity.

The solution focused on two main numbers to produce this results: 
1- the number of initial cars in a certain street.
2- the total numbers of cars going throught this street.

Intuitively if you have two streets with 5 cars on the first street and 2 cars on the second street. You want to open the first street for 5 seconds and then the second street for 2 seconds (or the second then the first). So i took the initial numbers of cars in the street as a factor. Then the second factor is the total number of cars passing by the street during the simulation. So i used the total number of cars to decide how long will i open this street. Then i used both factors to decide the time for opening this street. I added a weight for each factor and then played around with this weight from 0% to 100% and each percentage / balance generated a better score for a specific test case.


## [Google Hash Code Link](https://codingcompetitions.withgoogle.com/hashcode/)
