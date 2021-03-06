# Processes and Threads
- Two basic units of concurrent execution: 1) processes; 2) threads
- Processing tie for a single core is shared among processes and threads. Called 'time slicing'
- Assign different tasks to different processors/cores

## Processes
- Self contained execution environment
- Private set of basic runtime resources
- (i.e. - own memory space)
- Processes can communicate w/ each other (IPC)
- IPC can be used both intra & inter systems
- Java: ProcessBuilder

## Threads
- lightweight processes
- creating thread requires fewer resources
- threads exist inside a process
- each process has at least one thread
- threads share a process's resources, including memory and open files
- need synchronization because 2 threads can manipulate SAME memory

## Multithreading

### Advantages
- More responsive software
- several things @ same time
- improve performance
- means better resource util

## Disadvantages
- Costs involved
- Not always better
- More complex
- Hard to detect errors
- Data inconsistency bc threads touch same memory locations
- Switching between threads is expensive
  * CPU saves local data, pointers, etc... from current thread
  * then loads data of other thread
  * each thread has own distinct memory location
- For small problems --> unnecessary. Maybe slower than single threading.
- Optimal # of threads. Too many, and running time goes up sharply


## Life Cycle of Threads
- New
- Runnable
- Running
- Waiting
- Dead
- (born, started, runs, dies)


# Deadlock
- 2 or more competing actions are waiting for the other to finish --> so neither ever can
- Databases --> processes within each transaction update two rows, but in oppsoite order
- OS --> When a process/thread enters a waiting state because a resource requested is being held by another waiting process, which in turn is waiting for another resource from another waiting process
- Detect with a Directed Graph --> if cycle ==> deadlock
- Use DFS to check if there is a cycle

# Livelock
- Threads can act in response to action of another thread
- If the other thread's action is a response to action of ANTOHER thread --> livelock!
- Livelocked threads are unable to make further progress, but they are NOT blocked
- Instead, they are too busy responding to each other to resume work


# Multithreading
- Re-entrant lock has same behavior as sync'd blocks
- Extended features
  * can make the lock fair --> prevent thread starvation. Sync'd blocks are unfair by default
  * check whether given lock is held or not
  * get list of threads waiting for given lock
  * sync'd blocks don't require try/catch/finally


# Semaphores
- Dijkstra in 1962
- ADT used for controlling accesss to a common resource
- Important in OS
- Record of how many units of a particular resource are available... wait until the resource becomes avail
- Counting semaphore, allows an arbitrary resource count
- Binary semaphores (0,1)
- Counts HOW MANY resources are free
- Does NOT count WHICH resources are free


# Mutexes
- Binary semaphore
- Have concept of 'owner'
- Only process that locked mutex is supposed to unlock it
- Provide priority inversion safety --> possible to promote a higher priority task to the owner, if necessary
- Can provide deletion safety

