## An agent-based model of warfare-induced residence change

This is an agent-based model of warfare-induced residence change. This was developed for publication "An Agent-Based Model of Warfare-Induced
Post-Marital Residence Change" (Moravec et al. 2019).

### Implementation

Model is implemented in Java using [repast simphony](https://repast.github.io/) framework.

### Description
This model simulate a community of villages which is under attack by another non-simulated community. This cause villages on the border of the community to change from patrilocal to matrilocal residence to replenish the number of males (and thus warriors) through marriage. This mechanism was suggested by W. T. Divale in *Matrilocal Residence in Pre-Literate Society* (1984). The suggested model also describes migration, internal warfare and cycle by which society turns back from matrilocal to patrilocal residence. These steps are however not implemented. The model was called 'simple_model' to characterize this notion during model development.

### Usage
To run this model, you will need Java (1.8), the repast simphony framework (2.3.1) and Eclipse (Kepler version), since the repast framework integrates into (a specific version) of Eclipse IDE. See [repast simphony documentation](https://repast.github.io/) for more information.
