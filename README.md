Program a well-known ordinary differential
equation (ODE) known as Romeo and Juliet over a TCP/IP client-server
architecture. In a more real scenario; i will be building a 1 client (the Playwriter)
and 2 single-threaded servers (Romeo and Juliet) concurrent system. The
servers (Romeo and Juliet) are analgous in the service they provide although
each one solves one of the equations of the Romeo and Juliet Ordinary differential equation. They are
single-threaded. The client, i.e. the Playwriter, will be responsible to com-
municate with the servers to get the ODE values over time (i.e. iterations).
For each verse of the play (i.e. iteration of the ODE), the Playwriter will
request the service from both servers and annotate their answers in the novel.
At the end of the iterations, the novel will be dumped into a .csv file.
