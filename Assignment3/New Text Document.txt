# Assignment 3
Currently the check buffer length and add to buffer work great

## To Run:
1. Compile all
2. In one terminal run "java ProcessServer"
3. In another terminal run "java ProducerClient"
4. Issue commands from the ProducerClient terminal
5. Do not connect more than 2 ProducerClients to a server (it will crash)

## Known bugs:
- After running command 4 once, the next time any command is run the "RUN" server directive gets eaten up
- Buffer overrun is possible and easy
- Command 2 does not do anything yet