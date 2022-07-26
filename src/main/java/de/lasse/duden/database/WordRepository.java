package de.lasse.duden.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface WordRepository extends CrudRepository<Word, String> {
    public List<Word> findAll();
    @Query("select utilization, count(*) from Word group by utilization order by COUNT(*) desc")
    public List<String> findAllDistinctUtilization();

}
