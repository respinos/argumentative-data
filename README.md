## What is this?

An attempt to build a Spring Data JDBC application just by talking to the **Copilot Agent**. The agent was essentially given a simplified version of the schema from `dor-console-py` and instructed to generate a Spring Data JDBC application to support basic catalog queries.

Aside: this project was bootstraped with the IDEA "new project" wizard and that's why it's using Thymeleafe templates instead of Pebble.

## Why do this?

I _know_ SQL. 

I am _so very frustrated_ with ORMs.

Spring Data JDBC and the repository pattern make sense but also immediately _get in my way_. This catalog we're building is _just a bunch of queries_ and the amount of infrastructure Spring expects just to render a bunch of rows _feels_ fundamentally wrong: this is the easy bit! What is doing the hard work going to look like?

This was an attempt to tell the bot what I expected the catalog to do and then hopefully get a typical Spring application out of it that wouldn't be tainted by me wanting to chuck it all for a `DBI` query.

## How did it go?

What a mixed bag! Using the **agent** is a different experience than using chat as a better interface to disparate documentation.

It didn't generate any tests (so the bot and I have that in common, alas).

It was **phenomenally stupid** about pagination --- half my question was to get a better sense of the constraints of Spring's paginagion and the bot ignored all that and **loaded all the root rows** and then sliced them. When I had it calculate the size of a root object by also aggregating the sizes of all child objects it repeatedly insisted on **loading all the rows** and _then_ filtering out the root rows and _then_ slicing them for pagination.

Finally, I'd asked it to refactor the code to support `PossibleObject` being an aggregate (these words!) and it did that but then the importer would fail. Copilot/ChatGPT-4.1 **could not debug this**. There's nothing that rankles like asking a question based on what the logs are showing and having the bot say _I should have told you about..._ Eventually I consulted Gemini and that bot diagnosed the problem.

_What was the problem?_ If you say you're aggregating a `List` of things, then that child table needs an index column (surprise!).

## TL,DR

SQL is awesome. ORMs are a pain. But we can smooth this divide by either 

- pre-calculate sizes during cataloging
  - not making the object an aggregate
  - then reporting the root object size would only involve aggregating the cached sizes of its child data
- create a view that does all the aggregating
