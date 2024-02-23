package ru.itmo.soa.soaspacemarine.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.util.ProxyUtils
import java.io.Serializable

@MappedSuperclass
abstract class BaseEntity<T : Serializable> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: T? = null

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as BaseEntity<*>

        return this.id != null && this.id == other.id
    }

    override fun hashCode() = 25

    override fun toString(): String {
        return "${this.javaClass.simpleName}(id=$id)"
    }

    companion object {
        private val serialVersionUID = -5554308939380869754L
    }
}