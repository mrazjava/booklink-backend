# Notes on AWS
Amazon Web Services is used as the hosting platform of booklink application. Upon merge to `master` branch, sources 
are verified, built and packaged into a docker image which is pushed to AWS.A new version of a [task definition](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task_definitions.html) is rendered and uploaded to AWS. This in turn tells the [service](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ecs-service.html) to re-deploy the latest docker image as defined in the new specification of a task definition.

`booklink` uses the following AWS features: 

## [Amazon ECR](https://aws.amazon.com/ecr/)
Elastic Container Registry is used by `booklink` to upload the docker image it builds during a github build pipeline. Once hosted on Amazon (ECR), it is then deployed using ECS.

## [Amazon ECS](https://aws.amazon.com/ecs/)
Elastic Container Service allows booklink to deploy a docker image with pre-defined specifications such as CPU, memory, number of instances and auto scaling.

* Cluster: `booklink-ec2lx-cluster`
* Service: `booklink-backend-svc` (staging)
* Task Definition: `booklink-backend-ec2-taskdef`
* Container: `booklink-backend-contr`

## [Amazon EC2](https://aws.amazon.com/ec2/)
Elastic Compute Cloud is used to define actual VM hosts (machines) that ultimately host and run contenerized application (defined via ECS). A service in ECS always ends up running on a machine provided by EC2.

## [Amazon IAM](https://aws.amazon.com/iam/)
Identity and Access Management is used to defined users, roles and access associated with individual services. For example, booklink application has its own user `booklink` which operates within certain context of roles and policies defined using IAM.

## [Amazon CloudWatch](https://aws.amazon.com/cloudwatch/)
Used by booklink to monitor resources and check logs.

# Maven Runs
We can run the app locally against AWS resources if we have access. AWS IAM user `booklink` credentials are necessary 
in order to establish successful connection:
```
mvn clean spring-boot:run -Dspring-boot.run.profiles=local,aws-db-pre1 -Dspring-boot.run.jvmArguments="-Daws.accessKeyId= -Daws.secretKey= -Daws.region=eu-central-1"
```
You can either create you own AWS could environment or talk to me to get RDS booklink access.