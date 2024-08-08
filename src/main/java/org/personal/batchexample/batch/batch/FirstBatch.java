package org.personal.batchexample.batch.batch;

import lombok.RequiredArgsConstructor;
import org.personal.batchexample.batch.entity.AfterEntity;
import org.personal.batchexample.batch.entity.BeforeEntity;
import org.personal.batchexample.batch.repository.AfterRepository;
import org.personal.batchexample.batch.repository.BeforeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class FirstBatch {
    // table to table
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final BeforeRepository beforeRepository;
    private final AfterRepository afterRepository;

    @Bean
    public Job firstJob(){

        return new JobBuilder("firstJob",jobRepository)
                .start(
                        //step 정의
                    firstStep()
                )
//                .next(
//                        // 다음 step 필요시
//                )
                .build();
    }
    @Bean
    public Step firstStep(){

        return new StepBuilder("firstStep",jobRepository)
                .<BeforeEntity, AfterEntity>chunk(
                    // 끊어 읽을 최대 단위
                    10,platformTransactionManager
                )
                .reader(
                    //읽는 메소드 자리
                    beforeReader()
                 )
                .processor(
                    //처리 메소드 자리
                    middleProcessor()
                 )
                .writer(
                    //쓰기 메소드 자리
                    afterWriter()
                 )
                .build();
    }

    @Bean
    public RepositoryItemReader<BeforeEntity> beforeReader(){

        // JPA 기반의 Reader
        return new RepositoryItemReaderBuilder<BeforeEntity>()
                .name("BeforeReader")
                .pageSize(10)// 10개씩
                .methodName("findAll")
                .repository(beforeRepository)
                //순서는 sort를 통해 id가 작은 순서대로 읽어옴
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor(){

        return new ItemProcessor<BeforeEntity, AfterEntity>() {

            @Override
            public AfterEntity process(BeforeEntity item) throws Exception {
                AfterEntity afterEntity=new AfterEntity();
                afterEntity.setUsername(item.getUsername());
                return afterEntity;
            }
        };
    }

    @Bean
    public RepositoryItemWriter<AfterEntity> afterWriter(){

        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }
}
