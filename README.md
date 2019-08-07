# Week Two Weekend - RecyclerView #

## Screnshots ##
<p float="left">
  <img src="/screenshots/2019-08-05%2011.46.25.jpg" width="200" />
  <img src="/screenshots/2019-08-05%2011.46.06.jpg" width="200" />
  <img src="/screenshots/2019-08-05%2011.45.34.jpg" width="200" />
  <img src="/screenshots/2019-08-05%2011.45.16.jpg" width="200" />
</p>

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

#### 2. Define lazy loading. ####
* loading only enough content to show on the part of the screen that is shown
* allows users to see content faster such as in RecyclerView instead of waiting for all the data to be loaded like ListView does

#### 3. What is an item decorator in RecyclerViews? ####
* allows special drawing and layout offset to specific item views from the dataset
* useful for drawing dividers between items, highlights, visual grouping boundaries

#### 4. What is the View Holder Pattern? ####
* allows access to each list item view without the need to look up
* avoids frequent calls to findViewById()

#### 5. How do you implement a item touch helper for the RecyclerView? ####
* used for swipe to dismiss and drag & drop
* first create ItemTouchHelper.Callback which lets you control the touch event
* it provides methods you can override and choose how to handle the touches

## Coding
#### Create an app which is a directory for all the animals in a zoo. </br> ####
1. <b>Activity one:</b> Make a homepage for zoo information
2. <b>Activity two:</b> ListView with list of all the animals
3. <b>Activity three:</b> RecyclerView with a list of all the animals in that category
4. <b>Activity four:</b> Detail of that animal selected from the list. It should have the detail of that animal and play a sound of that animal
