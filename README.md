# README

Repository of UC Berkeley CS61b Sp21

*[Project 0 - 2048](https://github.com/mingqianyu0524/cs61b/tree/master/proj0)*

*[Project 1 - Deque](https://github.com/mingqianyu0524/cs61b/tree/master/proj1)*

*[Project 2 - GitLet](https://github.com/mingqianyu0524/cs61b/tree/master/proj2)*


# GITLET README

A brief summary of the major design decisions made when building the program and the rationales behind those decisions.

What the project scope is and what the program is capable of doing is detailed in the project spec.

__Project spec: https://sp21.datastructur.es/materials/proj/proj2/proj2__

__Author: Mingqian Yu__

---

## Classes

### Provided

`Dumpable`, `DumpObj`, and `Utils` are classes provided by the instructors, `Repository` implemented `Dumpable` to print out useful information for debugging unit tests.

`Utils` provided some handy functions to read / write objects and files.

`Main` is also given, but only as an empty framework.

### Repository

The repository where the most important status info are kept: staging area, HEAD pointer and current branch.

In addition, all the business logic in Main are implemented here, since it's easier to look up the current state of the staging area for various commands (like `add` and `commit` for example).

* Fields
  - currentBranch - Name of the branch this repository is currently on. (String)
  - staging - Staging area of the current repository, see Staging class for details. (Staging)
  - head - HEAD commit of the current repository, see Commit class for details. (Commit)

### Commit

The commit objects are used to track metadata of commits. The tree is simplified from original git per project spec since we are assuming the working directory as flat (depth = 1).

Each commit will be on more than one branches after branching, and have more than one parents after merging. Depending on which branch the commit is on, its parent might differ. The parent of the branch is acquired by looking up the `parents` table in the commit object. Methods like `addParent()` and `removeParent()` are used to update the table in the case of merging, branching, and removal of branches.

* Fields
  - serialVersionUID: A unique ID used to deserialize the commit object (Long)
  - message: The attached message to this commit (String)
  - timestamp: The time when this commit was made (String)
  - tree: mapping from `filename` (String) -> `blobname` (String), which is the sha1 of file content. (Map<String, String>)
  - parents: Name of the branch this commit is on -> Parent commit sha1. (Map<String, String>)

### Staging

The staging area singleton of the repository, since staging area is a part of the repository, I put its declaration inside the Repository class. Note that the inner class needs to implement serializable otherwise the Repository object will have trouble serializing itself.

* Fields
  - stagedForAddition: mapping from file name to blob name
  - stagedForRemoval: set of file names staged to remove

### Constants

A class used to store constants like certain directories and error messages.

---

## Data Structures and Algorithms

- There is a hard bound time complexity requirement on command `Merge` (O(NlgN+D)), where N is the total number of ancestor commits for two branches, and D is the total amount of data in all the files under these commits. 
The `Merge` process can be break down into two steps:
  1. Find the split point of the two branches;
  2. For each file in the target, head, and split commit, compare if the file exists, modified, or unchanged, and take actions based on the result.


- The runtime of step2 is easy to analyze, since we are iterating over D files, and for each file we are making conditional checks which takes constant time, the overall runtime is O(D); The runtime of step1 depends on the underlying algorithms, consider the example below where commit b being the split point, there are two algorithms we could implement to find b:

```
# Example two-pointer algorithm

                HEAD, master, p1             
                      |                       p1 = e
              c   d   e                       p2 = i
              * - * - *                       set = []
             /                                both p1 & p2 are not in set
    a * - * b                             
             \                                
              * - * - * - *           
              f   g   h   i
                          |
                       branch, p2
                       
   
   ========================= after a few iterations  =========================
   
                       
                  HEAD, master             
                      |                       p1 = a
              c   d   e                       p2 = b
    p1        * - * - *                       set = [b,c,d,e,f,g,h,i]
    |        /                                p2 is already in set
    a * - * b - p2
             \                                split = b
              * - * - * - *
              f   g   h   i
                          |
                       branch                  
```

  1. Two pass iteration: 
   
     - If we iterate over the commits on the master and the other branch, put their UIDs in a list (first pass), then reverse the list. We can go through the list again (second pass), and the first two adjacent entries that have the same UIDs is the splitting point. The time complexity is bounded by `Collections.reverse()` in Java which is O(N).

  2. Two pointers (selected):

     - Alternatively, we can use pointer p1, p2 to iterate over two branches one step at a time, and put every commit UID in a set, if we later see a parent commit already exists in the set, it means we have found the splitting point, i.e. the latest common ancestor commit of those two branches. This algorithm will take O(N) time in the worst case, and space complexity is also linear, same as in previous approach.

  - The performance of the two pointers algorithm is better, as in most cases, we do not need to visit every single commit on a branch. The first algorithm will suffer performance issue if we have two long branches to merge, and the latest common ancestor is relatively close to the tips of branches.  
  

  - On the other hand, _real_ git also has a tree field in its `Commit` object to capture the current working directory snapshot. Implementing it will require diving into the git source code. Currently, this information is kept in a hashmap, this is sufficient as we are living under the assumption that the repository contains only files without sub-folders, but not good enough for any real-world scenarios, will come back and update this part later.


  - As a reference, this is how real git organized its objects: https://git-scm.com/book/en/v2/Git-Internals-Git-Objects

---

## Persistence

### Repository

The Repository object is stored directly under `.gitlet/(GITLET_DIR)`, with the filename being Repository. When user enters the current working directory, Main will load the Repository object from there with the latest state.

The Repository is a singleton, so I'm less worried about the storage efficiency of the class. We can improve it however, by changing the head field type from Commit to String, which records the UID of the commit HEAD points to. Doing so we rid the potential space usage of a long list of commits. 

HEAD is referenced a lot within the Repository which raises a question: Does it worth the effort to deserialize the HEAD commit everytime we reference it? The serialization requires loading object from the disk, and it hurts the loading time performance-wise.

### Commits

The commits are kept under `.gitlet/commits/(COMMITS_DIR)`, filenames correspond to a 40-byte SHA1 UID, generated from the commit timestamp + commit message.

As the number of commits grow up with the Repository, it's important to optimize the persistence of the object: the parent commits are stored as a String of their UIDs instead of a Commit object which potentially could be pointing to a long list of parent commits.

### Branches

Branches info are kept in `.gitlet/branches/(BRANCHES_DIR)`, filename corresponds to branch name, and file content corresponds to the branch HEAD (A commit ID).

When it comes to tracing back the commit history of a certain branch, simply read the commit id from the branch file, deserialize it and go to its parent commit while the parent commit is not null.

It would be better under certain constraints performance-wise if I refactored the Repository to include a map of branch name to head commit ID, since most repositories don't have a lot of branches, and they contain only a reference to the head commit. The repository can preload the branch info into the map when `load()` is called in Main, store the branch info, and clean up the map when saving the repository.

Another way to refactor how branches are stored could be getting rid of the branches directory and keep all branch information inside the repository.

### Blobs

Blobs are stored in `.gitlet/blobs/(BLOBS_DIR)`, filename corresponds to file blob SHA1, I avoid using filename directly since the content of file might differ from commit to commit.

The actual content of the blob file will be stored in the form of string. This might not be good enough if we are trying to store files in non-text formats. Maybe store the actual contents as byte stream instead?
