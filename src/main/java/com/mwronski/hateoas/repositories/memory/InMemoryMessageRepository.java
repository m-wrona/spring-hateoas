package com.mwronski.hateoas.repositories.memory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mwronski.hateoas.model.Message;
import com.mwronski.hateoas.repositories.Repository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.mwronski.hateoas.log.Tracer.tracer;
import static java.lang.String.format;

/**
 * Basic implementation of repository that manages messages. Repository keeps all data in-memory thus
 * should used with deliberation. Implementation is thread safe.
 *
 * @author Michal Wronski
 * @date 27-05-2014
 */
@Scope("singleton")
@Component
public final class InMemoryMessageRepository implements Repository<Message> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<String> documents = new ArrayList<String>();
    private final Map<String, String> documentsById = new HashMap<String, String>();

    @Override
    public Message create(Message entity) {
        tracer(this).debug("Creating message: %s", entity);
        lock.writeLock().lock();
        try {
            String entityID = UUID.randomUUID().toString();
            entity.setEntityId(entityID);
            String json = new Gson().toJson(entity);
            documents.add(json);
            documentsById.put(entityID, json);
            return entity;
        } catch (Exception e) {
            tracer(this).error("Couldn't create message: %s", entity, e);
            throw new RuntimeException(format("Couldn't create message: %s", entity));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Message find(String id, String... columns) {
        tracer(this).debug("Find message - id: %s", id);
        lock.readLock().lock();
        try {
            String json = documentsById.get(id);
            return json == null ? null : fromJson(json, columns);
        } catch (Exception e) {
            tracer(this).error("Couldn't find message - id: %s", e, id);
            throw new RuntimeException(format("Couldn't find message - id: %s", id), e);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Message> get(int start, int rowCount, String... columns) {
        tracer(this).debug("Getting messages - start: %d, rowCount: %d", start, rowCount);
        lock.readLock().lock();
        try {
            List<Message> list = new ArrayList<Message>(rowCount);
            for (int i = start; i < documents.size() && list.size() < rowCount; i++) {
                list.add(fromJson(documents.get(i), columns));
            }
            return list;
        } catch (Exception e) {
            tracer(this).error("Couldn't get messages - start: %d, rowCount: %d", e, start, rowCount);
            tracer(this).error("Couldn't get messages - columns: %s", (Object[]) columns);
            throw new RuntimeException(format("Couldn't get messages - start: %d, rowCount: %d", start, rowCount), e);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Deserialize message from JSON representation
     *
     * @param json
     * @param columns optional columns that should be returned in results. If not given all will be taken.
     * @return
     */
    private Message fromJson(String json, String... columns) {
        Gson gson = new GsonBuilder().setExclusionStrategies(new ColumnsFilter(columns)).create();
        return gson.fromJson(json, Message.class);
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return documents.size();
        } finally {
            lock.readLock().unlock();
        }
    }

}
