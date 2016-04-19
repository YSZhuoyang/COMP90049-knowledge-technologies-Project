## Knowledge Technology project - Reviewing movies
* Matching movie reviews with movie titles.
* Ranking movies based on reviews associated with the title.
* Evaluate the review sentiment using Maven api (whether the title associated with the review is positive or negative)

## Approximate matching algorithms
* NGram algorithm for matching tokens (words or phrases) extracted from titles and reviews.
* Smith Waterman algorithm calculating local edit distance.

## File list
    ./
    README.md
    KTProject.iml
    .gitignore
    
    ./src
    TestDriver.java
    TokenRetriever.java
    LocalEditDistanceAnalyzer.java
    NGramAnalyzer.java
    ReviewsFileLoader.java
    TitleFileLoader.java
    TitleReviewMatcher.java
    Evaluator.java
    
    ./lib
    ...     // External libraries
    
    ./Data
    film_titles.txt
    
    ./Data/revs
    1.txt
    ...
    49995.txt
    
    ./Output
    NGram matches.txt
    Local edit distance matches.txt
    evaluations.txt

## How to run file
* Install and open Intellij
* Clone the project, navigate to the project directory and open the project
* Go to 'File -> Project structure -> Libraries' to check whether maven library dependencies are imported
* Add an application run/debug entry in edit configuration and select TestDriver.java
* By default, the file loaders will read all files into memory however both analyzers only process 20 reviews, for the purpose of speed and convenience for manually checking correctness. But it can be configered by modifying the input parameter in 'TestDriver.java'. Details can be found in 'TestDriver.java' file
* After running the project, output files named 'NGram matches.txt', 'Local edit distance matches.txt' and 'evaluations.txt' will appear under './Output/' directory

## Author
* Sangzhuoyang Yu

##Terms of Use

    Andrew L. Maas, Raymond E. Daly, Peter T. Pham, Dan Huang, Andrew Y. Ng, and Christopher Potts. (2011).
    Learning Word Vectors for Sentiment Analysis.
    The 49th Annual Meeting of the Association for Computational Linguistics (ACL 2011).
