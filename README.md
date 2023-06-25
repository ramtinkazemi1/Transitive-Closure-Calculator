# Transitive Closure Calculator

The Transitive Closure Calculator is a Java program that computes the transitive closure of a database relation representing fund transfers between accounts. The program utilizes the Java Database Connectivity (JDBC) interface to interact with the database and perform the necessary computations.

## Transitive Closure Calculation Process

The transitive closure calculation is performed through the following steps:

1. **Establishing Database Connection**: The program uses the JDBC driver to establish a connection with the Postgres database server.

2. **Retrieving Fund Transfer Information**: The program executes SQL queries to fetch the necessary information from the database tables: `transfer`, `customer`, and `account`. The `transfer` table contains records of fund transfers between accounts, while the `customer` and `account` tables store customer and account details, respectively.

3. **Semi-Naive Evaluation**: The transitive closure computation utilizes semi-naive evaluation, which involves iteratively expanding the reachability information until convergence is achieved. The program starts by creating a new table named `delta`, which will store the changes in reachability information during each iteration.

4. **Initial Computation**: The program performs the initial computation of the transitive closure by inserting tuples into the `influence` table. For each tuple (src, tgt) in the `transfer` table, it inserts a corresponding tuple into the `influence` table, signifying that customer src influences customer tgt directly.

5. **Iterative Computation**: The program enters a loop that continues until convergence is reached. In each iteration, the program updates the `delta` table to reflect the changes in reachability information.
   
   - The program performs a self-join on the `influence` table using the `who` and `whom` columns, creating a new temporary table.
   
   - The temporary table contains tuples representing new indirect influences between customers. It does not include any tuples that already exist in the `influence` table.
   
   - The program inserts the tuples from the temporary table into the `delta` table.
   
   - The program updates the `influence` table by inserting the tuples from the `delta` table.

6. **Convergence Check**: After each iteration, the program checks for convergence by examining the `delta` table. If the `delta` table is empty, it means that no new tuples were added, indicating that the transitive closure has been computed.

7. **Clean-up and Maintenance**: The program drops the `influence` table at the beginning to ensure clean execution. It also drops any auxiliary tables, including the `delta` table, at the end to facilitate re-running the program without conflicts.

## Programming Considerations

The program has the following considerations:

- The program utilizes the Postgres JDBC driver (v42.5.0) to connect to the Postgres database server. Make sure to download and configure the driver accordingly.

- The JDBC connection settings, including the database URL, username, and password, need to be correctly configured in the program to establish the database connection.

- The program executes SQL queries to fetch data from the database and perform the necessary computations. It uses SQL join operations and insert statements to update the tables.

- To ensure efficiency and minimize data transfer between the Java program and the database server, the program performs most of the computation within the database. It utilizes SQL commands executed on the server rather than transferring data to the client for processing.

- The program creates and maintains auxiliary tables, such as the `delta` table, to track changes during the iterative computation process.

## Running the Program

To run the Transitive Closure Calculator program, follow these steps:

1. Set up a Postgres database server and create a database.

2. Download the Postgres JDBC driver (v42.5.0) and configure it in your Java development environment.

3. Import the Java program into your development environment.

4. Configure the JDBC connection settings in the program to match your database configuration, including the database URL, username, and password.

5. Build and run the Java program.

Please ensure that you have followed the necessary setup and configuration steps before executing the program.

**Note**: The program does not require any manual input or output. The results will be stored in the `influence` table within the database for further analysis.

If you have any questions or encounter any issues while running the program, feel free to reach out for assistance.
