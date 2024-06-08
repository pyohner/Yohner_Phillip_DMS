<h1> Disney Attractions DMS </h1>
<p>In this version, the application will load sample attraction data from a text file, list the attractions, and then present a menu for the user.  The user can choose to add an attraction, remove an attraction, update an attraction, rate an attraction, list the Top 10 rated attractions, or list all attractions. They can also choose to exit the program.</p>
<ul>
<li>Adding an attraction will ask the user for a name, description, location, type, height restriction, thrill level, and opening date.</li>
<li>Updating an attraction will ask the user for the ID number of the attraction they want to update, then which attribute they want to update before asking for the updated information.</li>
<li>Removing an attraction will first ask the user if they want to use the attraction ID number or the attraction name and location. Then the user enters the information of the attraction they want to remove.</li>
<li>Rating an attraction will ask the user for the ID number of the attraction they want to remove or rate.</li>
<li>Listing the Top 10 attractions will calculate the average rating for each attraction and then list the top 10 of a rating sorted list.</li>
<li>Listing all attractions will display all the attractions in the database.</li>
</ul>

<h2>Flowchart for CRUD Operations and Custom Actions</h2>
<ol>
<li>Add Attraction</li>
<ul>
<li>Input: Attraction details</li>
<li>Process: Add to database</li>
<li>Output: Success (Attraction added) or Failure (Error message)</li>
</ul>
<li>Update Attraction</li>
<ul>
<li>Input: Attraction ID, Attribute, updated details</li>
<li>Process: Update database entry</li>
<li>Output: Success (Attraction updated) or Failure (Error message)</li>
</ul>
<li>Remove Attraction</li>
<ul>
<li>Input: Attraction ID - or - Attraction name, location</li>
<li>Process: Remove from database</li>
<li>Output: Success (Attraction removed) or Failure (Error message)</li>
</ul>
<li>Rate Attraction</li>
<ul>
<li>Input: Attraction ID, rating</li>
<li>Process: Add to rating list</li>
<li>Output: Success (Attraction updated) or Failure (Error message)</li>
</ul>
<li>List Top 10 Attractions</li>
<ul>
<li>Input: Request top 10 list</li>
<li>Process: Calculate average rating, sort list, and collect top 10 rated attractions</li>
<li>Output: Display top 10 list or Error message</li>
</ul>
<li>List All Attractions</li>
<ul>
<li>Input: Request list</li>
<li>Process: Retrieve all attractions</li>
<li>Output: Display all attractions or Error message</li>
</ul>
<li>Exit</li>
<ul>
<li>Input: Request to exit</li>
<li>Process: Terminate application</li>
<li>Output: Success (Application closed)</li>
</ul>
</ol>
