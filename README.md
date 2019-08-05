# Homework Week Two Weekend #

## Research ##
#### 1. What is the difference in recyclerView and listView? ####
Major advantages RecyclerView has over ListView: </br>
* used for scrolling list of elements for large data sets or data that changes frequently

RecyclerView ViewHolder </br>
* each view holder displays a single item with a view
* only as many needed to disaplay on-screen plus a few extra to be ready when they are scrolled to
* view holders that are off-screen the longest can be re-bound to new data

LayoutManager </br>
* allows us how to choose difference ways to display the list such as in a horizontal list or grid view which cant be done using ListView

2. Define lazy loading. </br>
3. What is an item decorator in RecyclerViews? </br>
4. What is the View Holder Pattern? </br>
5. How do you implement a item touch helper for the RecyclerView? </br>

## Coding
Create an app which is a directory for all the animals in a zoo. </br>
1. Activity one: Make a homepage for zoo information </br>
2. Activity two: ListView with list of all the animals. </br>
3. Activity three: RecyclerView with a list of all the animals in that category </br>
4. Activity four: Detail of that animal selected from the list. It should have the detail of that animal and play a sound of that animal. </br>
