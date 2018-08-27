my_data <- read.csv('tmp.csv')
kruskal.test(value ~ group, data=my_data)
