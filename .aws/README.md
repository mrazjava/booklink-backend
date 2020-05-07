# Notes on AWS
Amazon Web Services is used as the hosting platform of booklink application.

## Overview
Upon merge to `master`, sources are verified, built and packaged into a docker image which is pushed to AWS [ECR](https://aws.amazon.com/ecr/) with 
_master_ label. A new version of a [task definition](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task_definitions.html) 
is rendered and uploaded to AWS [ECS](https://aws.amazon.com/ecs/). This in turn tells the [service](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-ecs-service.html) 
to re-deploy the latest image as defined in the new specification of a task definition.

Upon issued [github release](https://github.com/mrazjava/booklink-backend/releases) (which tags master branch), sources are built, tested and a final version of a docker image 
is assembled and pushed to ECR. The version is derived from version defined via github release interface (eg: `vX.X.X`). 
A live release is made manually by creating a new revision of a live task pointing to a final version of the docker 
image.

Some more details on the backend topology follow. 

## Topology
Domain is managed via [Route53](https://aws.amazon.com/route53/). [Application load balancers](https://docs.aws.amazon.com/elasticloadbalancing/latest/application/introduction.html) 
(ALBs) are attached to subdomain alias records (`be.` and `pre-be`). These backend ALBs listen only on port `443` which forwards traffic to respective 
[target group](https://docs.aws.amazon.com/elasticloadbalancing/latest/application/load-balancer-target-groups.html) (TG) linked 
to an EC2 instance running the backend container. TGs are linked to ECS [services](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs_services.html) 
which in turn manage task definitions. The EC2 instances are elastic in nature as they are launched via 
[launch configurations](https://docs.aws.amazon.com/autoscaling/ec2/userguide/LaunchConfiguration.html) and managed by 
[auto scaling groups](https://docs.aws.amazon.com/autoscaling/ec2/userguide/AutoScalingGroup.html) (ASGs), one group for `pre` 
and `live`. As ASGs are linked to TGs, whenever new EC2 instance is spawned up it automatically joins a target group so 
that ECS services are always aware of container instances running them.

> IMPORTANT: EC2 instances are Amazon AMI ECS optimized. They are not listed as default options and MUST be searched 
> in Amazon marketplace.

Without ECS optimized image all dependencies such as docker and Amazon ECS agent must be installed on the instance. In 
addition, non ECS optimized image will not automatically join ECS cluster of choice defined in the launch configuration!

## Aws CLI
In the perfect world, a docker container on local should run the same as the container on EC2. I found this 
unforutnately to not be the case. Sometimes it's necessary to spend time SSH'ed into EC2 instance troubleshooting 
running container so here are some useful commands:

Verify secret
```
aws secretsmanager describe-secret --secret-id MyDemoSecret
aws secretsmanager get-secret-value --secret-id MyDemoSecret --version-stage AWSCURRENT
``` 