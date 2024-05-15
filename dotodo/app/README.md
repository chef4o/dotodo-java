# DOTODO TASK MANAGER

**Initial setup via React + Vite**

## Concept

The DOTODO app aims to provide various functionalities for assistance with our everyday personal tasks in three dimensions: 
-	free-hand notes 
-	to-do tasks and checklists
-	calendar events
Each functionality has its own concept and design oriented towards intuitiveness, flexibility and detail. 
Functionalities
1. Core note elements
   - title              // non-mandatory field to give idea of the content
   - content            // mandatory field containing the task details
   - trackProgress      // functionality that will give the option to track the task. On ‘Start’ it sets a start date and starts counting days toward                                                                   completion. The ‘Start” button changes to ‘Stop’. On pressing Stop it records competedOn date and the ‘Stop’ button
                           changes to ‘Continue’. Pressing it remove the completedOn date. [IN DEVELOPMENT]
   - startDate
   - dueDate            //gives you the option to set a due date which will trigger alerts and styles [IN DEVELOPMENT]
   - completedOn
   - isArchived         //will let you archive the note, blurring its content and putting it in the archive bin. Can be reopened later on. [IN DEVELOPMENT]
   - owner
   - sharedWith         //will let you share the task with friends [IN DEVELOPMENT]

2. Core checklist elements:
   - type                //choose between General to-do and Shopping list [IN DEVELOPMENT]
   - title
   - elements
      - content
      - dueDate
      - status           //Not started/In progress/Done for general tasks; Pending/Done for shopping list
   - sharedWith          //will let you share the task with friends [IN DEVELOPMENT]
   - isArchived
   - dueDate

3. Core event elements:  [IN DEVELOPMENT]
   - type                //choose between Birthday, Name day, Vacation, Event, etc.
   - title
   - icon
   - content
   - date
   - isArchived


## Installation
To install the app, clone the repository and run npm install to install the necessary dependencies.

``git clone <repository-url>
cd <repository-folder>
npm install``

## Usage
Start the app using:

``npm start
Navigate to localhost:3000 in your browser to interact with the app.``
